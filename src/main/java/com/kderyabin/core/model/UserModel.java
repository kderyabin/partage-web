package com.kderyabin.core.model;


import java.util.UUID;

import com.kderyabin.core.storage.entity.UserEntity;

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
    
    private String login;
    
    private String pwd;
    
    private String token;
    

	public UserModel() {
		super();
	}
    
	public UserModel(String login, String pwd) {
		this.login = login;
		this.pwd = pwd;
	}
	
	/**
	 * 
	 */
	public void generateId() {
		id = UUID.randomUUID().toString();
	}
}
