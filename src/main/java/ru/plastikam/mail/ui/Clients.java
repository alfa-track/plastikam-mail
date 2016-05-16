package ru.plastikam.mail.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.Client;
import ru.plastikam.mail.repository.ClientRepository;

import java.io.Serializable;
import java.util.List;

@Service
@Scope(scopeName = "view")
public class Clients implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Clients.class);

    @Autowired
    ClientRepository clientRepository;

    private List<Client> clients;

    private List<Client> clientsFiltered;

    public Clients() {

    }

    public List<Client> getClients() {
        if (clients == null) {
            clients = clientRepository.findAll();
        }
        return clients;
    }

    public List<Client> getClientsFiltered() {
        return clientsFiltered;
    }

    public void setClientsFiltered(List<Client> clientsFiltered) {
        this.clientsFiltered = clientsFiltered;
    }
}
