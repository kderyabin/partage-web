package com.kderyabin.core.storage.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kderyabin.core.storage.entity.UserEntity;

@SpringBootTest
class UserRepositoryTest {
	
	final private Logger LOG = LoggerFactory.getLogger(UserRepositoryTest.class);
	
	@Autowired
	UserRepository repository;
	
	@Test
	void findByLoginPwd() {
		String login = "a@x.com";
		String pwd = "password";
		
		UserEntity entity = new UserEntity();
		entity.setId( UUID.randomUUID().toString());
		entity.setLogin(login);
		entity.setPwd(pwd);
		
		UserEntity saved = repository.saveAndFlush(entity);
		assertNotNull(saved);
		LOG.info("Created entity: " + saved.toString());;
		
		UserEntity search = repository.findByLoginPwd(login, pwd);
		assertNotNull(search);
		LOG.info("Fetched entity: " + search.toString());;
	}

}
