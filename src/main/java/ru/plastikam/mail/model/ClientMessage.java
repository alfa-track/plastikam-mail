package ru.plastikam.mail.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class ClientMessage extends AbstractEntity {

    @Column(columnDefinition = "TEXT")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String clientName;

    @Column(columnDefinition = "TEXT")
    private String messageBody;

    @Column(columnDefinition = "TEXT")
    private String source;

    @ManyToOne
    private Client client;

    private Date date;

    @Column(columnDefinition = "TEXT")
    private String phone;

    private Boolean knownClient;

    @ManyToOne
    private Region region;

    private long ticket;

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

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Boolean getKnownClient() {
        return knownClient;
    }

    public String getKnownClientString() {
        if (getKnownClient() == null)
            return "?";
        return getKnownClient() ? "Да" : "Нет";
    }

    public void setKnownClient(Boolean knownClient) {
        this.knownClient = knownClient;
    }

    @Column(columnDefinition = "TEXT")
    private String resolution = "";

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void addResolution(String s) {
        resolution += s + "\n";
    }


    private boolean error = false;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setError(Exception e) {
        setError(true);
        setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    @Column(columnDefinition = "TEXT")
    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getTicket() {
        return ticket;
    }

    public void setTicket(long ticket) {
        this.ticket = ticket;
    }
}
