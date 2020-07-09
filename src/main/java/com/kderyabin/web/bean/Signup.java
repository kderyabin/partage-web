package com.kderyabin.web.bean;

import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**	
 * 
 * SignUpDTO contains sign up form data.
 * 
 * @author Konstantin Deryabin
 */
@ToString
@Getter
@Setter
public class Signup {
	/**
	 * User name
	 */
	@Size( min =2, max = 100, message = "error.name_too_big")
	private String name;
	/**
	 * User login
	 */
	@Email(message = "error.email_invalid")
	private String login;
	/**
	 * User password
	 */
	@Size( min = 8, max = 20,  message = "error.password_size")
	private String pwd;

	/**
	 * Password confirmation to ensure there is no mistake in a password.
	 */
	@NotEmpty( message = "error.password_confirm_empty")
	private String confirmPwd;
}
