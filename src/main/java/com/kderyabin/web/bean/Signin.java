package com.kderyabin.web.bean;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
public class Signin implements Serializable {

	private static final long serialVersionUID = 6947039856789018538L;
	/**
	 * User login
	 */
	@Getter
	@Setter
	@Email(message = "error.email_invalid")
	private String login;

	/**
	 * User password
	 */
	@Getter
	@Setter
	@Size( min = 8, max = 20,  message = "error.password_size")
	private String pwd;	
}
