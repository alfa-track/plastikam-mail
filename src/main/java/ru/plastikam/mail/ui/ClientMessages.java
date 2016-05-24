package ru.plastikam.mail.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.ClientMessage;
import ru.plastikam.mail.repository.AbstractRepository;
import ru.plastikam.mail.repository.ClientMessageRepository;

@Service
@Scope(scopeName = "view")
public class ClientMessages extends InMemoryTableBacking<ClientMessage> {

    @Autowired
    ClientMessageRepository repository;

    public ClientMessages() {
    }

    @Override
    protected AbstractRepository<ClientMessage> getRepository() {
        return repository;
    }

}
