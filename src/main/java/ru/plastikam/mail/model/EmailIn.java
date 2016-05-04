package ru.plastikam.mail.model;

import javax.persistence.Entity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class EmailIn extends Email {

    private String resolution = "";

    public String getSource() {

        Pattern pattern = Pattern.compile(".*\\+(\\d*)\\@.*");

        Matcher matcher = pattern.matcher(getRecipient());

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";


    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
