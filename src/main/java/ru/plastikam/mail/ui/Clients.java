package ru.plastikam.mail.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.Client;
import ru.plastikam.mail.repository.AbstractRepository;
import ru.plastikam.mail.repository.ClientRepository;

@Service
@Scope(scopeName = "view")
public class Clients extends InMemoryTableBacking<Client> {

    @Autowired
    ClientRepository clientRepository;

    public Clients() {
    }

    @Override
    protected AbstractRepository<Client> getRepository() {
        return clientRepository;
    }

}
