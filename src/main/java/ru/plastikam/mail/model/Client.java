package ru.plastikam.mail.model;

import javax.persistence.Entity;

@Entity
public class Client extends AbstractEntity {

    private String email;

    public Client(String email) {
        this.email = email;
    }

    public Client() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
