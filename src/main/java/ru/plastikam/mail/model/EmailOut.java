package ru.plastikam.mail.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class EmailOut extends Email {

    @ManyToOne(fetch = FetchType.EAGER)
    ClientMessage clientMessage;

    public EmailOut() {
    }

    public EmailOut(String body) {
        this.messageBody = body;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(ClientMessage clientMessage) {
        this.clientMessage = clientMessage;
    }
}
