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
import javax.mail.internet.MimeMultipart;
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
                emailIn.setMessageBody("" + messages[i].getContent().toString());
                emailIn.setMessageBody("" + getTextFromMessage(messages[i]));

                result.add(emailIn);

//                messages[i].get
//                messages[i].setFlag(new Flags.Flag() );

                messages[i].setFlag(Flags.Flag.SEEN, true);
            }

            folder.close(true);

            store.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getTextFromMessage(Message message) throws Exception {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws Exception {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public void processMail() {

        logger.info("Проверка почты ... ");

        List<EmailIn> emailIns = read();

        for (EmailIn emailIn : emailIns) {

            process(emailIn);
        }

    }

    private void process(EmailIn emailIn) {
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
//        message += "[SOURCE]: " + emailIn.getSource() + "\n\n";
        message += "[TICKET]:  " + ((long) (Math.random() * Long.MAX_VALUE)) + "\n\n";
//        message += "[FORM]: " + "\n\n";
        message += "[DATE]: " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(emailIn.getDate()) + "\n\n";
        message += "[ROISTATID]: " + emailIn.getSource() + "\n\n";
        message += "[NAME]: " + "\n\n";
//        message += "[PHONE]: " + "\n\n";
        message += "[EMAIL]: " + emailIn.getSender() + "\n\n";
        message += "[BODY]: " + emailIn.getMessageBody() + "\n\n";

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

            apacheEmail.setTextMsg(emailOut.getMessageBody());
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
