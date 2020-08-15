package com.kderyabin.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 * Settings for  data
 */
@ToString
@Getter @Setter
public class Settings {
    @NotEmpty( message = "error.language_is_required")
    String language;

    @NotEmpty( message = "error.currency_is_required")
    String currency;

    @Size( min =2, max = 100, message = "error.name_invalid")
    private String name;

    @Email(message = "error.email_invalid")
    private String login;
}
