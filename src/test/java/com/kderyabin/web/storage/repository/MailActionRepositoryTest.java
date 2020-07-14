package com.kderyabin.web.storage.repository;

import com.kderyabin.web.storage.MailAction;
import com.kderyabin.web.storage.entity.MailActionEntity;
import com.kderyabin.web.storage.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

@SpringBootTest
class MailActionRepositoryTest {
    final private Logger LOG = LoggerFactory.getLogger(MailActionRepositoryTest.class);

    @Autowired
    MailActionRepository mailActionRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void findByToken() {
        UserEntity user = new UserEntity();
        user.setId( UUID.randomUUID().toString());
        user.setName("Joe");
        user.setLogin("x@x.com");
        user.setPwd("password");

        user = userRepository.save(user);

        MailActionEntity entity = new MailActionEntity();
        entity.setAction(MailAction.CONFIRM);
        entity.setUser(user);
        entity.setToken(UUID.randomUUID().toString());

        MailActionEntity saved =  mailActionRepository.save(entity);
        LOG.debug("Saved action:  " + saved.toString());

        MailActionEntity found = mailActionRepository.findByToken(entity.getToken());
        LOG.debug("Found action:  " + found.toString());
        assertNotNull(found);
        assertEquals(entity.getId(), found.getId());
    }
}