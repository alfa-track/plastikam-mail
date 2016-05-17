package ru.plastikam.mail.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Client extends AbstractEntity {

    @Column(columnDefinition = "TEXT")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String clientName;

    private Date firsContact;

    private String source;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @ManyToOne
    private Region region;

    public Client(String email, String clientName, Date firsContact, String source, Region region) {
        this.email = email;
        this.clientName = clientName;
        this.firsContact = firsContact;
        this.source = source;
        this.region = region;
    }

    public Client() {
    }

    public Date getFirsContact() {
        return firsContact;
    }

    public void setFirsContact(Date firsContact) {
        this.firsContact = firsContact;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
