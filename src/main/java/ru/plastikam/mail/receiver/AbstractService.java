package ru.plastikam.mail.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.repository.*;

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

    @Autowired
    protected ClientMessageRepository clientMessageRepository;



}
