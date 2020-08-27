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

    private static final long serialVersionUID = -1205511984773487423L;
    /**
     * Password confirmation to ensure there is no mistake in a password.
     */
    @NotEmpty( message = "error.password_confirm_empty")
    private String confirmPwd;
}
