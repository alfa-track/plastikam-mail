package ru.plastikam.mail.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Client extends AbstractEntity {

    @Column(columnDefinition = "TEXT")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String clientName;

    private Date firsContact;

    private String source;

    private String phone;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    protected List<ClientMessage> messages = new ArrayList<>();

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @ManyToOne
    private Region region;

    public Client(String email, String clientName, Date firsContact, String source, Region region, String phone) {
        this.email = email;
        this.clientName = clientName;
        this.firsContact = firsContact;
        this.source = source;
        this.region = region;
        this.phone = phone;
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

    @Override
    public String toString() {
        return "Client{" +
                "email='" + email + '\'' +
                ", clientName='" + clientName + '\'' +
                ", firsContact=" + firsContact +
                ", source='" + source + '\'' +
                ", region=" + region +
                ", getCreateDate=" + getCreateDate() +
                ", getUpdateDate=" + getUpdateDate() +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ClientMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ClientMessage> messages) {
        this.messages = messages;
    }

}
