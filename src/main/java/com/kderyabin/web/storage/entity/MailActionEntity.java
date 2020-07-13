package com.kderyabin.core.storage.entity;

import com.kderyabin.core.MailAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Table with pending actions triggered by email.
 */
@Entity
@ToString
@Getter @Setter
@Table(name = "mail", schema = "sharings")
public class MailActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private UserEntity user;

    /**
     * Action
     * @see MailAction
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MailAction action;

    /**
     * Action token: UUID
     */
    @Column(name = "token", length = 36, nullable = false)
    private String token;

    /**
     * Creation time.
     */
    @Column(name = "creation", nullable = false)
    private Timestamp creation = new Timestamp(System.currentTimeMillis());
}
