package ru.plastikam.mail.repository;

import org.springframework.stereotype.Repository;
import ru.plastikam.mail.model.Client;

@Repository
public interface ClientRepository extends AbstractRepository<Client> {

    int countByEmail(String email);

    Client findOneByEmailIgnoreCase(String email);

}
