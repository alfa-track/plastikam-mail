package ru.plastikam.mail.receiver;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.configuration.Config;
import ru.plastikam.mail.model.EmailIn;
import ru.plastikam.mail.model.EmailOut;
import ru.plastikam.mail.repository.EmailInRepository;
import ru.plastikam.mail.repository.EmailOutRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class Receiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    EmailInRepository emailInRepository;

    @Autowired
    EmailOutRepository emailOutRepository;

    public List<EmailIn> read() {

        try {

            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);

            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", Config.Mail.In.username, Config.Mail.In.password);

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            Message messages[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            int n = messages.length;

            List<EmailIn> result = new ArrayList<>();

            logger.debug("Есть " + n + " новых писем");

            for (int i = 0; i < messages.length; i++) {

                EmailIn emailIn = new EmailIn();
                emailIn.setDate(messages[i].getReceivedDate());
                emailIn.setRecipient(messages[i].getAllRecipients()[0].toString());

                InternetAddress from = ((InternetAddress) messages[i].getFrom()[0]);

                emailIn.setSender(from.getAddress());
                emailIn.setSubject(messages[i].getSubject());
//                emailIn.setBody(messages[i].getContent().toString());

                result.add(emailIn);

                messages[i].setFlag(Flags.Flag.SEEN, true);
            }

            folder.close(true);

            store.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void processMail() {

        logger.info("Проверка почты ... ");

        List<EmailIn> emailIns = read();

        for (EmailIn emailIn : emailIns) {

            process(emailIn);
        }

    }

    private void process(EmailIn emailIn) {
        1
        EmailOut emailOut = translate(emailIn);

        if (emailInRepository.countBySender(emailIn.getSender()) == 0) {
            send(emailOut);
            emailIn.setResolution("Письмо пересланно");
        } else {
            emailIn.setResolution("Письмо от известного отправителя пропущено");
            logger.info("Письмо от" + emailIn.getSender() + " пропущено");
        }

        emailIn = emailInRepository.save(emailIn);
        emailOut = emailOutRepository.save(emailOut);

        logger.info("Письмо переслано");

    }

    private EmailOut translate(EmailIn emailIn) {
        String message = "";

        message += "[TYPE]: 3\n\n";
        message += "[SOURCE]: " + emailIn.getSource() + "\n\n";
        message += "[TICKET]:  " + "\n\n";
        message += "[FORM]: " + "\n\n";
        message += "[DATE]: " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(emailIn.getDate()) + "\n\n";
        message += "[ROISTATID]: " + "\n\n";
        message += "[NAME]: " + "\n\n";
        message += "[PHONE]: " + "\n\n";
        message += "[EMAIL]: " + emailIn.getSender() + "\n\n";
        message += "[BODY]: " + emailIn.getBody() + "\n\n";

        EmailOut emailOut = new EmailOut(message);
        return emailOut;
    }

    public static void send(EmailOut emailOut) {
        try {

            final HtmlEmail apacheEmail = new HtmlEmail();
            apacheEmail.setHostName("smtp.gmail.com");
            apacheEmail.setSmtpPort(587);
            apacheEmail.setAuthenticator(new DefaultAuthenticator(Config.Mail.Out.username, Config.Mail.Out.password));
            apacheEmail.setFrom(Config.Mail.Out.username, Config.Mail.Out.username, "utf-8");
            apacheEmail.setSubject("Пересылка письма от нового клиента");
            apacheEmail.setCharset("utf-8");
            apacheEmail.setSocketTimeout(2000);
            apacheEmail.addTo(Config.Mail.Out.recipient);
            apacheEmail.setStartTLSRequired(true);

            apacheEmail.setTextMsg(emailOut.getBody());
            apacheEmail.send();

        } catch (Exception e) {
            emailOut.addComment("Ошибка при отправке email сообщения: " + e.toString());
            throw new RuntimeException("Ошибка при отправке email сообщения", e);
        }
    }

    @Scheduled(fixedDelay = Config.Mail.scheduleDelayMs)
    public void task() {
        processMail();
    }
}
