package ru.plastikam.mail.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@MappedSuperclass
public abstract class Email extends AbstractEntity {

    protected String sender;

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected List<Recipient> recipients = new ArrayList<>();

    protected String subject;

    @Column(columnDefinition = "TEXT")
    protected String messageBody;

    protected Date date;

    protected String comment = "";

    public Email() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void addComment(String s) {
        comment += s;
    }
}