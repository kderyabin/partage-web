package com.kderyabin.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kderyabin.core.model.UserModel;
import com.kderyabin.core.storage.entity.UserEntity;
import com.kderyabin.core.storage.repository.UserRepository;

@Service
public class UserManager {
	final private Logger LOG = LoggerFactory.getLogger(UserManager.class);
	
}
