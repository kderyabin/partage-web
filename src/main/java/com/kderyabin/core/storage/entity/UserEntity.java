package com.kderyabin.core.storage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * User entity contains user credential data. 
 * @author Konstantin Deryabin
 *
 */
@ToString
@Entity
@Table(name = "user")
public class UserEntity {
	/**
	 * User unique Id.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    @Getter @Setter
    private Long id;
    
    /**
     * User login (currently email).
     */
    @Getter @Setter
    @Column(name = "login", nullable = false, length = 100, unique = true)
    private String login;
    
    /**
     * User password hash.
     */
    @Getter @Setter
    @Column(name = "pwd")
    private String pwd;
    
    /**
     * User token for remote access (synchronization).
     * The token serves as an identifier in case of remote access.
     */
    @Getter @Setter
    @Column(name = "token", unique = true)
    private String token;
}
