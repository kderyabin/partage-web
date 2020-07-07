package com.kderyabin.core.services;

import com.kderyabin.core.MailAction;
import com.kderyabin.core.model.MailActionModel;
import com.kderyabin.core.model.UserModel;
import com.kderyabin.core.storage.entity.MailActionEntity;
import com.kderyabin.core.storage.entity.UserEntity;
import com.kderyabin.core.storage.repository.MailActionRepository;
import com.kderyabin.core.storage.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Manages interaction with DB.
 */
@Service
public class AccountManager {
    final private Logger LOG = LoggerFactory.getLogger(AccountManager.class);

    UserRepository userRepository;
    MailActionRepository mailActionRepository;

	@Autowired
	public void setMailActionRepository(MailActionRepository mailActionRepository) {
		this.mailActionRepository = mailActionRepository;
	}
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

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
	 * Process few actions during account creation :
	 * 1. Creates user in DB
	 * 2. Creates confirmation mail action in DB
	 * @param model UserModel
	 * @return MailActionModel
	 */
	@Transactional
	public MailActionModel create(UserModel model) {
		LOG.debug("Start account creation");

		model.generateId();
		UserEntity user = getEntity(model);
		user = userRepository.save(user);

		MailActionEntity action = new MailActionEntity();
		action.setUser(user);
		action.setAction(MailAction.CONFIRM);
		action.setToken( UUID.randomUUID().toString() );

		action = mailActionRepository.save(action);

		MailActionModel actionModel = getModel(action);
		LOG.debug("End account creation");
		return actionModel;
	}


	/**
	 * Updates existing user in database.
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
		target.setUser( getModel(source.getUser()));
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
		target.setUser( getEntity(source.getUser()));
		target.setCreation(source.getCreation());

		return target;
	}
}