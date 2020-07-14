package com.kderyabin.web.storage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * User entity contains user credential data.
 * 
 * @author Konstantin Deryabin
 *
 */
@ToString
@Entity
@Table(name = "user" )
@NamedNativeQuery(name = "UserEntity.findByLoginPwd", query = "select * from user where login = ?1 and pwd = ?2", resultClass = UserEntity.class)
public class UserEntity {
	/**
	 * User unique Id.
	 */
	@Id
	@Column(name = "user_id", nullable = false)
	@Getter
	@Setter
	private String id;

	/**
	 * User name
	 */
	@Getter
	@Setter
	@Column(name = "name", nullable = false, length = 100)
	private String name;

	/**
	 * User login (currently email).
	 */
	@Getter
	@Setter
	@Column(name = "login", nullable = false, length = 100, unique = true)
	private String login;

	/**
	 * User email confirmation status.
	 */
	@Getter
	@Setter
	@Column(name = "confirmed")
	private Boolean isConfirmed;

	/**
	 * User password hash.
	 */
	@Getter
	@Setter
	@Column(name = "pwd")
	private String pwd;

	/**
	 * User token for remote access (synchronization). The token serves as an
	 * identifier in case of remote access.
	 */
	@Getter
	@Setter
	@Column(name = "token", unique = true)
	private String token;
}
