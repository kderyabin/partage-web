package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import java.io.Serializable;

@ToString
@Getter @Setter
public class ResetRequest implements Serializable {
    private static final long serialVersionUID = 3539120400705931589L;
    /**
     * User email
     */
    @Email(message = "error.email_invalid")
    private String login;
}
