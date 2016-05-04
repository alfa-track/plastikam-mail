package ru.plastikam.mail.repository;

import org.springframework.stereotype.Repository;
import ru.plastikam.mail.model.EmailIn;

@Repository
public interface EmailInRepository extends AbstractRepository<EmailIn> {

    int countBySender(String sender);

}
