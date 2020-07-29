package com.kderyabin.web.model;

import com.kderyabin.web.storage.MailAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailActionModel that = (MailActionModel) o;
        return id.equals(that.id) &&
                user.equals(that.user) &&
                action == that.action &&
                token.equals(that.token) &&
                creation.equals(that.creation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, action, token, creation);
    }
}
