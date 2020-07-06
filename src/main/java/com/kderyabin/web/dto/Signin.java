package com.kderyabin.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Signin {
	
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
