package ru.plastikam.mail.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.Client;
import ru.plastikam.mail.model.EmailIn;
import ru.plastikam.mail.model.EmailOut;

import java.text.SimpleDateFormat;

@Service
public class MessageProcessor extends AbstractService {

    @Autowired
    EmailSender sender;

    public void process(EmailIn emailIn) {

        try {

            if (emailIn.isError())
                return;

            boolean resend = false;

//            emailIn.setRegion(regionRepository.findOneByMailPrefix(emailIn.parseRegion()));

//            if (emailIn.getRegion() == null) {
//                resend = false;
//                emailIn.addResolution("Нет региона по получателю " + emailIn.parseRegion());
//            } else {
//                if (!emailIn.getRegion().isRegion()) {
//                    resend = false;
//                    emailIn.addResolution("Ригиона получателя - не регион " + emailIn.parseRegion());
//                } else {
//                    emailIn.addResolution("В получателе указан регион " + emailIn.parseRegion());
//                }
//            }

            if (clientRepository.countByEmail(emailIn.getSender()) == 0) {
                emailIn.addResolution("Клинет новый");

                Client client = new Client(emailIn.getSender(), emailIn.getSenderName(), emailIn.getDate(), "mail+" + emailIn.parseSource(), emailIn.getRegion());

                clientRepository.save(client);
                emailIn.setClient(client);
                resend = true;
            } else {
                emailIn.addResolution("Клинет существует");
                Client client = clientRepository.findOneByEmail(emailIn.getSender());
                emailIn.setClient(client);
                resend = false;
            }

            emailIn = emailInRepository.save(emailIn);

            if (resend) {
                EmailOut emailOut = translate(emailIn);
                emailOut.setEmailIn(emailIn);
                emailOut = emailOutRepository.save(emailOut);
                emailOut = sender.send(emailOut);
            }

            logger.info("Письмо обработано");

        } catch (Exception e) {
            logger.error("", e);
            emailIn.setError(e);
            emailInRepository.save(emailIn);
        } finally {
        }

    }

    private EmailOut translate(EmailIn emailIn) {
        String message = "";

        message += "[TYPE]: 3\n\n";
//        message += "[SOURCE]: " + emailIn.getSource() + "\n\n";
        message += "[TICKET]:  " + ((long) (Math.random() * Long.MAX_VALUE)) + "\n\n";
//        message += "[FORM]: " + "\n\n";
        message += "[DATE]: " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(emailIn.getDate()) + "\n\n";
        message += "[ROISTATID]: " + emailIn.parseSource() + "\n\n";
        message += "[NAME]: " + "\n\n";
//        message += "[PHONE]: " + "\n\n";
        message += "[EMAIL]: " + emailIn.getSender() + "\n\n";
        message += "[REGION]: " + (emailIn.getRegion() == null ? "" : emailIn.getRegion().getMailPrefix()) + "\n\n";
        message += "[REGION_RU]: " + (emailIn.getRegion() == null ? "" : emailIn.getRegion().getName()) + "\n\n";

        message += "[BODY]: " + emailIn.getMessageBody() + "\n\n";

        EmailOut emailOut = new EmailOut(message);
        return emailOut;
    }


}
