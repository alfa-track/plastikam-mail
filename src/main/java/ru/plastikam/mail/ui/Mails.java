package ru.plastikam.mail.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.EmailIn;
import ru.plastikam.mail.receiver.EmailReceiver;
import ru.plastikam.mail.repository.AbstractRepository;
import ru.plastikam.mail.repository.EmailInRepository;
import ru.plastikam.mail.sys.FC;

@Service
@Scope(scopeName = "view")
public class Mails extends InMemoryTableBacking<EmailIn> {

    @Autowired
    EmailInRepository emailInRepository;

    @Autowired
    EmailReceiver emailReceiver;

    public Mails() {
    }

    @Override
    protected AbstractRepository<EmailIn> getRepository() {
        return emailInRepository;
    }

    public void process() {
        emailReceiver.processMail();
        FC.info("Почта обработана", "");
    }

}
