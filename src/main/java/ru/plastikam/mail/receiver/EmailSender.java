package ru.plastikam.mail.receiver;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.configuration.Config;
import ru.plastikam.mail.model.EmailOut;

@Service
public class EmailSender extends AbstractService {

    public EmailOut send(EmailOut emailOut) {
        try {

            final HtmlEmail apacheEmail = new HtmlEmail();
            apacheEmail.setHostName("smtp.gmail.com");
            apacheEmail.setSmtpPort(587);
            apacheEmail.setAuthenticator(new DefaultAuthenticator(Config.Mail.Out.username, Config.Mail.Out.password));
            apacheEmail.setFrom(Config.Mail.Out.username, Config.Mail.Out.username, "utf-8");
            apacheEmail.setSubject("Пересылка письма от нового клиента");
            apacheEmail.setCharset("utf-8");
            apacheEmail.setSocketTimeout(10000);
            apacheEmail.addTo(Config.Mail.Out.recipient);
            apacheEmail.setStartTLSRequired(true);

            apacheEmail.setTextMsg(emailOut.getMessageBody());
            apacheEmail.send();

            return emailOut;
        } catch (Exception e) {
            emailOut.addComment("Ошибка при отправке email сообщения: " + e.toString());
            throw new RuntimeException("Ошибка при отправке email сообщения", e);
        }
    }
}
