package ru.plastikam.mail.model;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class Email extends AbstractEntity {

    protected String sender;

    protected String recipient;

    protected String subject;

    protected String body;

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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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