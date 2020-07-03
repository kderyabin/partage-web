package com.kderyabin.web.dto;

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
public class SignupDTO {

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

	/**
	 * Password confirmation to ensure there is no mistake in a password.
	 */
	@Getter
	@Setter
	@NotEmpty( message = "error.password_confirm_empty")
	private String confirmPwd;
}
