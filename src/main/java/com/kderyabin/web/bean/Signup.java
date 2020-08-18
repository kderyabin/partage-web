package com.kderyabin.web.bean;

import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains sign up form data.
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
    @Size(min = 2, max = 100, message = "error.name_invalid")
    private String name;
    /**
     * User login
     */
    @Email(message = "error.email_invalid")
    private String login;
    /**
     * User password
     */
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*](?=\\S+$).{8,20}$",
            message = "error.password_invalid"
    )
    private String pwd;

    /**
     * Password confirmation to ensure there is no mistake in a password.
     */
    @NotEmpty(message = "error.password_confirm_empty")
    private String confirmPwd;
}
