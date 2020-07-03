package com.kderyabin.core.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kderyabin.core.storage.entity.UserEntity;

/**
 * Repository interface to work with user table.
 * @author Konstantin Deryabin
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	/**
	 * Finds user by login.
	 * @param login User login.
	 * @param pwd	User password.
	 * @return UserEntity instance or null if user is not found.
	 */
	UserEntity findByLogin(String login);
	/**
	 * Finds user by login and password.
	 * @param login User login.
	 * @param pwd	User password.
	 * @return UserEntity instance or null if user is not found.
	 */
	UserEntity findByLoginPwd(String login, String pwd);
}
