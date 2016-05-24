package ru.plastikam.mail.repository;

import org.springframework.stereotype.Repository;
import ru.plastikam.mail.model.ClientMessage;

@Repository
public interface ClientMessageRepository extends AbstractRepository<ClientMessage> {
}
