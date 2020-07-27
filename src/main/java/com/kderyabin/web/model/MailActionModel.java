package com.kderyabin.web.model;

import com.kderyabin.web.storage.MailAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 *  Model derived from {@link com.kderyabin.web.storage.entity.MailActionEntity}
 * @see com.kderyabin.web.storage.entity.MailActionEntity for detailed description of class fields.
 */
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
