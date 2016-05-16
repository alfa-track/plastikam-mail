package ru.plastikam.mail.model;

import javax.persistence.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class EmailIn extends Email {

    private boolean error = false;

    private String errorMessage = "";

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<EmailOut> getEmailsOut() {
        return emailsOut;
    }

    public void setEmailsOut(List<EmailOut> emailsOut) {
        this.emailsOut = emailsOut;
    }

    @OneToMany(mappedBy = "emailIn",fetch = FetchType.EAGER)
    private List<EmailOut> emailsOut;

    @ManyToOne(cascade = CascadeType.ALL)
    private Region region;

    @ManyToOne
    private Client client;

    private String resolution = "";

    public String parseSource() {

        Pattern pattern = Pattern.compile(".*\\+(\\d*)\\@.*");

        for (Recipient recipient : recipients) {
            Matcher matcher = pattern.matcher(recipient.getEmail());

            if (matcher.matches()) {
                return matcher.group(1);
            }
        }

        return "";

    }

    public String parseRegion() {

        Pattern pattern = Pattern.compile("([^\\+\\@]*)[\\+\\@].*");

        for (Recipient recipient : recipients) {
            Matcher matcher = pattern.matcher(recipient.getEmail());

            if (matcher.matches()) {
                return matcher.group(1);
            }
        }

        return "";

    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void addResolution(String s) {
        resolution += s + "\n";
    }

    public void setError(Exception e) {
        setError(true);
        setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
}
