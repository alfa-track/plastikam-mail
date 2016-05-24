package ru.plastikam.mail.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.receiver.EmailReceiver;

@Service
public class Schedule {

    @Autowired
    EmailReceiver emailReceiver;

    @Scheduled(fixedDelay = Config.Mail.scheduleDelayMs)
    public void task() {
        emailReceiver.processMail();
    }
}
