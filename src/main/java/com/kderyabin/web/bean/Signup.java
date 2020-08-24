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
public class Signup extends ResetPassword {
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
}
