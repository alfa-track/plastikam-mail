package ru.plastikam.mail.model;

import javax.persistence.Entity;

@Entity
public class EmailOut extends Email {

    public EmailOut(String body) {
        this.messageBody = body;
    }
}
