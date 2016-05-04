package ru.plastikam.mail.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.EmailIn;
import ru.plastikam.mail.receiver.Receiver;
import ru.plastikam.mail.repository.EmailInRepository;
import ru.plastikam.mail.sys.FC;

import java.io.Serializable;
import java.util.List;

@Service
@Scope(scopeName = "view")
public class Mails implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Mails.class);

    @Autowired
    EmailInRepository emailInRepository;

    @Autowired
    Receiver receiver;


    private List<EmailIn> emails;

    private List<EmailIn> emailsFiltered;

    public Mails() {

    }

    public List<EmailIn> getEmails() {
        if (emails == null) {
            emails = emailInRepository.findAll();
        }
        return emails;
    }

    public void process() {
        receiver.processMail();
        FC.info("Почта обработана", "");
    }


    public List<EmailIn> getEmailsFiltered() {
        return emailsFiltered;
    }

    public void setEmailsFiltered(List<EmailIn> emailsFiltered) {
        this.emailsFiltered = emailsFiltered;
    }
}
