package ru.plastikam.mail.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EmailOut extends Email {

    public EmailIn getEmailIn() {
        return emailIn;
    }

    public void setEmailIn(EmailIn emailIn) {
        this.emailIn = emailIn;
    }

    @ManyToOne
    EmailIn emailIn;

    public EmailOut() {
    }

    public EmailOut(String body) {
        this.messageBody = body;
    }
}
