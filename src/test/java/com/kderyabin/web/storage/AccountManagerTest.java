package com.kderyabin.web.storage;

import com.kderyabin.web.model.MailActionModel;
import com.kderyabin.core.model.PersonModel;
import com.kderyabin.web.model.UserModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.storage.multitenancy.TenantContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AccountManagerTest {
    final private Logger LOG = LoggerFactory.getLogger(AccountManagerTest.class);

    @Autowired
    AccountManager manager;

    @Autowired
    StorageManager storageManager;

    @Test
    void createUserSpace() {
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);

        MailActionModel actionModel = manager.create(new UserModel("James", "n@x.com", "azeazeazea"));
        manager.createUserWorkspace( actionModel.getUser().getId());

        TenantContext.setTenant(manager.getUserWorkspaceName(actionModel.getUser().getId()));

        PersonModel person = new PersonModel("James");
        person = storageManager.save(person);

        LOG.debug(person.toString());

        assertTrue(true);
    }

    @Test
    void connect(){
        TenantContext.setTenant(manager.getUserWorkspaceName("153da92a-e877-4d43-9a3c-8285c188360c"));

        //List<BoardEntity> list = boardRepository.findAll();
        List<PersonModel> list = storageManager.getPersons();

        LOG.debug("Persons list: " + list.toString());
    }
}