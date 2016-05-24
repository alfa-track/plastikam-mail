package ru.plastikam.mail.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.EmailOut;
import ru.plastikam.mail.receiver.EmailReceiver;
import ru.plastikam.mail.repository.AbstractRepository;
import ru.plastikam.mail.repository.EmailOutRepository;
import ru.plastikam.mail.sys.FC;

@Service
@Scope(scopeName = "view")
public class Sentmails extends InMemoryTableBacking<EmailOut> {

    @Autowired
    EmailOutRepository emailOutRepository;

    @Autowired
    EmailReceiver emailReceiver;

    public Sentmails() {
    }

    @Override
    protected AbstractRepository<EmailOut> getRepository() {
        return emailOutRepository;
    }

    public void process() {
        emailReceiver.processMail();
        FC.info("Почта обработана", "");
    }

}
