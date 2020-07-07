package com.kderyabin.core.model;

import com.kderyabin.core.MailAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@ToString
@Getter
@Setter
public class MailActionModel {

    private Long id;

    private UserModel user;

    private MailAction action;

    private String token;

    private Timestamp creation = new Timestamp(System.currentTimeMillis());
}
