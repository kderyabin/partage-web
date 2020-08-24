package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ToString
@Getter
@Setter
public class ResetPassword extends Password{
    /**
     * User password
     */
/*    @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*](?=\\S+$).{8,20}$", message = "error.password_invalid")
    private String pwd;*/

    /**
     * Password confirmation to ensure there is no mistake in a password.
     */
    @NotEmpty( message = "error.password_confirm_empty")
    private String confirmPwd;
}
