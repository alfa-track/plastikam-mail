package ru.plastikam.mail.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.EmailIn;
import ru.plastikam.mail.receiver.Receiver;
import ru.plastikam.mail.repository.AbstractRepository;
import ru.plastikam.mail.repository.EmailInRepository;
import ru.plastikam.mail.sys.FC;

@Service
@Scope(scopeName = "view")
public class Mails extends InMemoryTableBacking<EmailIn> {

    @Autowired
    EmailInRepository emailInRepository;

    @Autowired
    Receiver receiver;

    public Mails() {
    }

    @Override
    protected AbstractRepository<EmailIn> getRepository() {
        return emailInRepository;
    }

    public void process() {
        receiver.processMail();
        FC.info("Почта обработана", "");
    }

}
