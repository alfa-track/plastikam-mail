package ru.plastikam.mail.repository;

import org.springframework.stereotype.Repository;
import ru.plastikam.mail.model.Client;

import java.util.List;

@Repository
public interface ClientRepository extends AbstractRepository<Client> {

    int countByEmailIgnoreCase(String email);

    Client findOneByEmailIgnoreCase(String email);

    List<Client> findByEmailIgnoreCaseOrderByCreateDate(String email);

    int countByEmailEndsWithIgnoreCase(String domain);
}
