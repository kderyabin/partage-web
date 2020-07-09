package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ToString
@Getter
@Setter
public class ResetPassword {
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
