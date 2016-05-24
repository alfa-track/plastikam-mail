package ru.plastikam.mail.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.plastikam.mail.model.Client;
import ru.plastikam.mail.receiver.EmailReceiver;
import ru.plastikam.mail.repository.ClientRepository;
import ru.plastikam.mail.repository.EmailInRepository;
import ru.plastikam.mail.sys.FC;

import java.io.Serializable;
import java.util.List;

@Service
@Scope(scopeName = "view")
public class Admin implements Serializable {

    @Autowired
    EmailInRepository emailInRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    EmailReceiver emailReceiver;

    public Admin() {
    }

    @Transactional
    public void removeCaseDuplicates() {

        clientRepository.findAll().forEach(c -> {
            List<Client> clients = clientRepository.findByEmailIgnoreCaseOrderByCreateDate(c.getEmail());
            if (clients.size() > 1) {
                clients.forEach(System.out::println);
                clientRepository.delete(clients.stream().findFirst().get());
            }
        });

        FC.info("Выполнено", "");
    }

}
