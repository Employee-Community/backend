package com.community.backend.domain.mail.entity;

import com.community.backend.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "TBL_ADMIN_MAIL_HISTORY")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "sender_email", nullable = false, length = 100)
    private String senderEmail;

    @Column(name = "receiver_email", nullable = false, length = 100)
    private String receiverEmail;

    @Column(name = "subject", nullable = false, length = 100)
    private String subject;

    @Column(name = "text", nullable = false, length = 2000)
    private String text;

    @Column(name = "mail_code", nullable = false, length = 10)
    private String mailCode;

    public MailHistory(String senderEmail, String receiverEmail, String subject, String text, String mailCode) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.text = text;
        this.mailCode = mailCode;
    }
}
