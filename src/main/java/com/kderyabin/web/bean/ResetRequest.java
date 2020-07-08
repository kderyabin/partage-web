package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;

@ToString
@Getter @Setter
public class ResetRequest {
    /**
     * User email
     */
    @Email(message = "error.email_invalid")
    private String login;
}
