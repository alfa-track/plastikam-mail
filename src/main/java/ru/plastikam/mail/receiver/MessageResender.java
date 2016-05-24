package ru.plastikam.mail.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.misc.PublicMailHostings;
import ru.plastikam.mail.misc.Util;
import ru.plastikam.mail.model.Client;
import ru.plastikam.mail.model.ClientMessage;
import ru.plastikam.mail.model.EmailOut;

import java.text.SimpleDateFormat;

@Service
public class MessageResender extends AbstractService {

    @Autowired
    EmailSender sender;

    public void process(ClientMessage cm) {

        try {

            fillKnownClient(cm);
            fillClient(cm);

            clientMessageRepository.save(cm);

            if (cm.getKnownClient()) {
                cm.addResolution("Клиент известный");
                logger.info("Обращение пропущено");
            } else {
                cm.addResolution("Клиент новый");
                EmailOut emailOut = translate(cm);
                emailOut.setClientMessage(cm);
                sender.send(emailOut);
                cm.addResolution("Отправлено письмо о новом клиенте");
            }

        } catch (Exception e) {
            logger.error("", e);
            cm.setError(e);
        } finally {
            clientMessageRepository.save(cm);
        }

    }

    private void fillClient(ClientMessage cm) {
        if (clientRepository.countByEmailIgnoreCase(cm.getEmail()) == 0) {
            cm.addResolution("Контактное лицо новое");

            Client client = new Client(cm.getEmail(), cm.getClientName(), cm.getDate(), cm.getSource(), cm.getRegion(), cm.getPhone());

            clientRepository.save(client);
            cm.setClient(client);
        } else {
            cm.addResolution("Контактное лицо существует");
            Client client = clientRepository.findOneByEmailIgnoreCase(cm.getEmail());
            cm.setClient(client);
        }
    }

    private void fillKnownClient(ClientMessage cm) {
        boolean knownClient;
        String domain = Util.rget(cm.getEmail(), ".*@(.*)");
        if (PublicMailHostings.publicMailHostings.contains(domain)) {
            cm.addResolution("Общедоступная почта");
            knownClient = clientRepository.countByEmailIgnoreCase(cm.getEmail()) > 0;
        } else {
            cm.addResolution("Корпоративная почта");
            knownClient = clientRepository.countByEmailEndsWithIgnoreCase(domain) > 0;
        }
        cm.setKnownClient(knownClient);
    }

    private EmailOut translate(ClientMessage clientMessage) {
        String message = "";

        message += "[TYPE]: 3\n\n";
//        message += "[SOURCE]: " + clientMessage.getSource() + "\n\n";
        message += "[TICKET]:  " + clientMessage.getTicket() + "\n\n";
//        message += "[FORM]: " + "\n\n";
        message += "[DATE]: " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(clientMessage.getDate()) + "\n\n";
        message += "[ROISTATID]: " + clientMessage.getROISTATID() + "\n\n";
        message += "[NAME]: " + "\n\n";
//        message += "[PHONE]: " + "\n\n";
        message += "[EMAIL]: " + clientMessage.getEmail() + "\n\n";
        message += "[REGION]: " + (clientMessage.getRegion() == null ? "" : clientMessage.getRegion().getMailPrefix()) + "\n\n";
        message += "[REGION_RU]: " + (clientMessage.getRegion() == null ? "" : clientMessage.getRegion().getName()) + "\n\n";

        message += "[BODY]: " + clientMessage.getMessageBody() + "\n\n";

        EmailOut emailOut = new EmailOut(message);
        return emailOut;
    }

}
