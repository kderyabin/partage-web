package com.kderyabin.web.model;


import java.util.Objects;
import java.util.UUID;

import com.kderyabin.web.storage.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * User model derived from {@link UserEntity}
 * @see UserEntity for detailed description of class fields. 
 * @author Konstantin Deryabin
 *
 */
@ToString
@Getter
@Setter
public class UserModel {
	
    private String id;
    private String name;
    private String login;
    private String pwd;
	private Boolean isConfirmed;
    private String token;

	public UserModel() {
		super();
	}
    
	public UserModel(String name, String login, String pwd) {
		this.name = name;
		this.login = login;
		this.pwd = pwd;
	}
	
	/**
	 * Generates and sets an ID in UUID format.
	 */
	public void generateId() {
		id = UUID.randomUUID().toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserModel userModel = (UserModel) o;
		return Objects.equals(id, userModel.id) &&
				Objects.equals(name, userModel.name) &&
				Objects.equals(login, userModel.login) &&
				Objects.equals(pwd, userModel.pwd) &&
				Objects.equals(isConfirmed, userModel.isConfirmed) &&
				Objects.equals(token, userModel.token);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, login, pwd, isConfirmed);
	}
}
