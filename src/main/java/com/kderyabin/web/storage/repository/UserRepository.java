package com.kderyabin.web.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kderyabin.web.storage.entity.UserEntity;

/**
 * Repository interface to work with user table.
 * @author Konstantin Deryabin
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	/**
	 * Finds user by login.
	 * @param login User login.
	 * @return UserEntity instance or null if user is not found.
	 */
	public UserEntity findByLogin(String login);
	/**
	 * Finds user by login and password.
	 * @param login User login.
	 * @param pwd	User password.
	 * @return UserEntity instance or null if user is not found.
	 */
	public UserEntity findByLoginPwd(String login, String pwd);
}
