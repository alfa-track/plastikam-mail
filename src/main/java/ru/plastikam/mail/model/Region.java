package ru.plastikam.mail.model;

import javax.persistence.Entity;

@Entity
public class Region extends AbstractEntity implements Comparable<Region> {
    private String mailPrefix = "";

    private String name = "";

    private boolean isRegion = false;

    public Region() {
    }

    public Region(String mailPrefix, String name) {
        this.mailPrefix = mailPrefix;
        this.name = name;
        isRegion = true;
    }

    public Region(String mailPrefix, String name, boolean isRegion) {
        this.mailPrefix = mailPrefix;
        this.name = name;
        this.isRegion = isRegion;
    }

    public String getMailPrefix() {
        return mailPrefix;
    }

    public void setMailPrefix(String mailPrefix) {
        this.mailPrefix = mailPrefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return mailPrefix + "(" + name + ")";
    }

    public boolean isRegion() {
        return isRegion;
    }

    public void setRegion(boolean region) {
        isRegion = region;
    }

    @Override
    public int compareTo(Region o) {
        if (o == null) return -1;
        return mailPrefix.compareTo(o.mailPrefix);
    }


}
