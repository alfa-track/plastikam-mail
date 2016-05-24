package ru.plastikam.mail.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class EmailIn extends Email {

    private String senderName;

    @ManyToOne
    private Region region;

    @OneToOne
    private ClientMessage clientMessage;

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

//    public String parseRegion() {
//
//        Pattern pattern = Pattern.compile("([^\\+\\@]*)[\\+\\@].*");
//
//        for (Recipient recipient : recipients) {
//            Matcher matcher = pattern.matcher(recipient.getEmail());
//
//            if (matcher.matches()) {
//                return matcher.group(1);
//            }
//        }
//
//        return "";
//
//    }


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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
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

    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(ClientMessage clientMessage) {
        this.clientMessage = clientMessage;
    }
}
