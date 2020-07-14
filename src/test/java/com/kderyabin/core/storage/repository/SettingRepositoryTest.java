package com.kderyabin.core.storage.repository;

import com.kderyabin.core.storage.entity.SettingEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class SettingRepositoryTest {

    final private Logger LOG = LoggerFactory.getLogger(SettingRepositoryTest.class);

    @Autowired
    SettingRepository settingRepository;

    @Test
    void save(){
        SettingEntity entity = new SettingEntity();
        entity.setName("lang");
        entity.setValue("fr");
        LOG.debug("Before save:" + entity.toString());
        entity = settingRepository.save(entity);
        LOG.debug("After save:" + entity.toString());

        assertNotNull(entity.getId());
    }

}