package ru.plastikam.mail.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.repository.ClientRepository;
import ru.plastikam.mail.repository.EmailInRepository;
import ru.plastikam.mail.repository.EmailOutRepository;
import ru.plastikam.mail.repository.RegionRepository;

@Service
public abstract class AbstractService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected EmailInRepository emailInRepository;

    @Autowired
    protected EmailOutRepository emailOutRepository;

    @Autowired
    protected RegionRepository regionRepository;

    @Autowired
    protected ClientRepository clientRepository;


}
