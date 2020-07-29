package com.kderyabin.web.storage;

import com.kderyabin.web.error.MailTokenNotFoundException;
import com.kderyabin.web.error.UserNotFoundException;
import com.kderyabin.web.model.MailActionModel;
import com.kderyabin.web.model.UserModel;
import com.kderyabin.web.storage.entity.MailActionEntity;
import com.kderyabin.web.storage.entity.UserEntity;
import com.kderyabin.web.storage.multitenancy.TenantContext;
import com.kderyabin.web.storage.repository.MailActionRepository;
import com.kderyabin.web.storage.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages interaction with main database.
 */
@Service
public class AccountManager {
    final private Logger LOG = LoggerFactory.getLogger(AccountManager.class);

    EntityManagerFactory emf;
    UserRepository userRepository;
    MailActionRepository mailActionRepository;

    String userDbNamePattern = "user-%s";

    @Value("${app.user.schema.filename}")
    String userSchemaFileName;

    @Value("${app.database.type}")
    String databaseType;

    @Autowired
    public void setMailActionRepository(MailActionRepository mailActionRepository) {
        this.mailActionRepository = mailActionRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void setUserSchemaFileName(String userSchemaFileName) {
        this.userSchemaFileName = userSchemaFileName;
    }

    /**
     * Reads some data from a file.
     *
     * @param inputStream Initialized input stream to read from
     * @return Content of the file
     * @throws IOException
     */
    private ArrayList<String> readFromInputStream(InputStream inputStream) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        }
        return result;
    }

    /**
     * Returns user database name
     *
     * @param userId User id
     * @return User database name
     */
    public String getUserWorkspaceName(String userId) {
        return String.format(userDbNamePattern, userId);
    }

    /**
     * Generates sql for schema (database) choice.
     * Currently supported types: MYSQL|H2
     *
     * @param databaseName Schema (database) name
     * @return Sql statement.
     */
    public String getDatabaseChoiceSQL(String databaseName) {

        switch (databaseType) {
            case "MYSQL":
                return String.format("USE `%s`", databaseName);
            case "H2":
                return String.format("SET SCHEMA `%s`", databaseName);
            default:
                throw new RuntimeException("Database type is undefined in application settings:" + databaseType);
        }
    }

    /**
     * Generates SQL for DB schema creation.
     *
     * @param databaseName Database name.
     * @return SQL statement.
     */
    public String getDbCreateSQL(String databaseName) {
        // Common for H2 and MySQL.
        return String.format("create schema if not exists `%s`", databaseName);
    }

    /**
     * Creates user database.
     * @param userId user ID.
     */
    public void createUserDatabase(String userId) {
        LOG.debug("Start createUserDatabase");
        final String schemaName = getUserWorkspaceName(userId);
        LOG.info("User DB: " + schemaName);
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Session session = entityManager.unwrap(Session.class);

        session.doWork(connection -> connection.createStatement().execute(getDbCreateSQL(schemaName)));
        entityManager.getTransaction().commit();
        entityManager.close();
        LOG.debug("End createUserDatabase");
    }

    /**
     * Populates user database with tables.
     *
     * @param userId User id
     */
    public void populateUserDatabase(String userId) {
        LOG.debug("Start user database initialization");
        // Switch to user database
        TenantContext.setTenant(getUserWorkspaceName(userId));
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Session session = entityManager.unwrap(Session.class);

        final ArrayList<String> list;
        try {
            list = readFromInputStream(getClass().getResourceAsStream("/" + userSchemaFileName));
            session.doWork(connection -> {
                try {
                    list.forEach(sql -> {
                        try {
                            //LOG.debug("executing SQL: " + sql);
                            connection.createStatement().execute(sql);
                        } catch (SQLException throwables) {
                            LOG.warn(throwables.getMessage());
                        }
                    });
                } catch (Exception throwables) {
                    LOG.warn(throwables.getMessage());
                }
            });
        } catch (IOException e) {
            LOG.warn("Failed to execute sql commands: " + e.getMessage());
        }

        entityManager.getTransaction().commit();
        entityManager.close();
        // Switch back to main database
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        LOG.debug("End createUserSpace");
    }

    /**
     * Create user database with tables.
     * The process uses 2 different connections.
     *
     * @param userId User id
     */
    public void createUserWorkspace(String userId) {
       createUserDatabase(userId);
       populateUserDatabase(userId);
    }

    /**
     * Verifies if user exists in database.
     * Based on Example usage that mean all non null fields will be used to query the database.
     *
     * @param model UserModel instance
     * @return TRUE if user exists FALSE otherwise
     */
    @Transactional(readOnly = true)
    public boolean isUserExists(UserModel model) {
        Example<UserEntity> e = Example.of(getEntity(model));
        return userRepository.exists(e);
    }

    /**
     * Finds user in DB by login and password.
     *
     * @param login    User login.
     * @param password user password.
     * @return UserModel instance or null if user is not found.
     */
    @Transactional(readOnly = true)
    public UserModel findUserByLoginPassword(String login, String password) {
        UserEntity entity = userRepository.findByLoginPwd(login, password);
        return getModel(entity);
    }

    /**
     * Finds user in DB by login.
     *
     * @param login User ID.
     * @return UserModel instance or null if user is not found.
     */
    @Transactional(readOnly = true)
    public UserModel findUserByLogin(String login) {
        UserEntity entity = userRepository.findByLogin(login);
        if (entity==null) {
            return null;
        }
        return getModel(entity);
    }

    /**
     * Checks if user has an account and creates an action for the password reset.
     *
     * @param login User email.
     * @return MailAction instance
     */
    @Transactional
    public MailActionModel registerResetRequest(String login) {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User not found for login: " + login);
        }
        MailActionEntity action = new MailActionEntity();
        action.setUser(user);
        action.setAction(MailAction.RESET);
        action.setToken(UUID.randomUUID().toString());

        action = mailActionRepository.save(action);

        return getModel(action);
    }

    /**
     * Creates or updates user account and creates an email confirmation record.
     * Process few actions during account creation :
     * 1. Saves user in DB
     * 2. Creates confirmation mail action in DB
     *
     * @param model UserModel
     * @return MailActionModel
     */
    @Transactional
    public MailActionModel create(UserModel model) {
        LOG.debug("Start account creation");

        if (model.getId() == null) {
            model.generateId();
        }

        model.setIsConfirmed(false);
        UserEntity user = getEntity(model);
        user = userRepository.save(user);

        MailActionEntity action = new MailActionEntity();
        action.setUser(user);
        action.setAction(MailAction.CONFIRM);
        action.setToken(UUID.randomUUID().toString());

        action = mailActionRepository.save(action);

        MailActionModel actionModel = getModel(action);
        LOG.debug("End account creation");
        return actionModel;
    }

    /**
     * Updates existing user in database.
     *
     * @param model
     * @return
     */
    @Transactional
    public UserModel save(UserModel model) {
        LOG.debug("Start UserModel saving ");
        UserEntity entity = getEntity(model);
        entity = userRepository.saveAndFlush(entity);
        model = getModel(entity);
        LOG.debug("End UserModel saving");
        return model;
    }

    /**
     * Fetches received token in DB, activates user's account if the token is valid.
     *
     * @param token Mail validation token
     * @return Up to date instance of UserModel
     */
    @Transactional
    public UserModel activateAccount(String token) {
        LOG.debug("Start activateAccount: processing token " + token);
        MailActionEntity entity = mailActionRepository.findByToken(token);
        if (entity == null) {
            throw new MailTokenNotFoundException("Mail confirmation token not found: " + token);
        }
        LOG.debug("Found mail action record");
        entity.getUser().setIsConfirmed(true);
        UserEntity user = userRepository.save(entity.getUser());
        mailActionRepository.delete(entity);
        LOG.debug("End activateAccount");
        return getModel(user);
    }

    /**
     * Find an action by token.
     *
     * @param token Mail action token
     * @return
     */
    @Transactional(readOnly = true)
    public MailActionModel findMailActionByToken(String token) {
        return getModel(mailActionRepository.findByToken(token));
    }
    /**
     * Find an action by token.
     *
     * @param user  UserModel instance
     * @return      MailActionModel instance or null
     */
    @Transactional(readOnly = true)
    public MailActionModel findMailActionByUser(UserModel user) {
        return getModel(mailActionRepository.findByUser(getEntity(user)));
    }
    /**
     * Deletes an action in DB.
     *
     * @param action MailActionModel
     */
    @Transactional
    public void deleteMailAction(MailActionModel action) {
        mailActionRepository.delete(getEntity(action));
    }

    /**
     * Converts UserEntity into UserModel.
     *
     * @param source UserEntity instance.
     * @return UserModel instance.
     */
    public UserModel getModel(UserEntity source) {

        if (source == null) {
            return null;
        }

        UserModel target = new UserModel();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setLogin(source.getLogin());
        target.setPwd(source.getPwd());
        target.setToken(source.getToken());
        target.setIsConfirmed(source.getIsConfirmed());

        return target;
    }

    /**
     * Converts UserModel into UserEntity.
     *
     * @param source UserModel instance.
     * @return UserEntity instance or null if source parameter is null.
     */
    public UserEntity getEntity(UserModel source) {

        if (source == null) {
            return null;
        }

        UserEntity target = new UserEntity();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setLogin(source.getLogin());
        target.setPwd(source.getPwd());
        target.setToken(source.getToken());
        target.setIsConfirmed(source.getIsConfirmed());

        return target;
    }

    /**
     * Converts MailActionEntity into MailActionModel.
     *
     * @param source MailActionEntity instance.
     * @return MailActionModel instance.
     */
    public MailActionModel getModel(MailActionEntity source) {

        if (source == null) {
            return null;
        }

        MailActionModel target = new MailActionModel();
        target.setId(source.getId());
        target.setAction(source.getAction());
        target.setToken(source.getToken());
        target.setUser(getModel(source.getUser()));
        target.setCreation(source.getCreation());

        return target;
    }

    /**
     * Converts UserModel into UserEntity.
     *
     * @param source UserModel instance.
     * @return UserEntity instance or null if source parameter is null.
     */
    public MailActionEntity getEntity(MailActionModel source) {

        if (source == null) {
            return null;
        }

        MailActionEntity target = new MailActionEntity();
        target.setId(source.getId());
        target.setAction(source.getAction());
        target.setToken(source.getToken());
        target.setUser(getEntity(source.getUser()));
        target.setCreation(source.getCreation());

        return target;
    }

}