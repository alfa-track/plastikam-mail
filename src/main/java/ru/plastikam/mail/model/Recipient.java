package ru.plastikam.mail.model;

import javax.persistence.Entity;

@Entity
public class Recipient extends AbstractEntity {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public Recipient(String email) {
        this.email = email;
    }

    public Recipient() {
    }


}
