package com.kderyabin.core.model;


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
	
    private Long id;
    
    private String login;
    
    private String pwd;
    
    private String token;
}
