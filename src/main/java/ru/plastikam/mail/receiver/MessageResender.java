package ru.plastikam.mail.receiver;

import org.apache.commons.lang3.StringUtils;
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
            if (cm.getSource().equals("Заявка с сайта (Старая)")) {
                cm.addResolution("Заявка с сайта в старом формате пропущена");
                logger.info("Обращение пропущено");
            } else if (cm.getKnownClient()) {
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
//        if (cm.getSource().equals("Заявка с сайта (Старая)")) {
//            cm.addResolution("Клиент из заявки с сайта в старом формате пропущен");
//            logger.info("Обращение пропущено");
//        } else
        if (clientRepository.countByEmailIgnoreCase(cm.getEmail()) == 0) {
            cm.addResolution("Контактное лицо новое");

            Client client = new Client(cm.getEmail(), cm.getClientName(), cm.getDate(), cm.getSource(), cm.getRegion(), cm.getPhone());

            clientRepository.save(client);
            cm.setClient(client);
        } else {
            cm.addResolution("Контактное лицо известно");
            Client client = clientRepository.findOneByEmailIgnoreCase(cm.getEmail());

            updateClientFromMessage(client, cm);

            cm.setClient(client);
        }
    }

    private void updateClientFromMessage(Client client, ClientMessage cm) {

        if (StringUtils.isBlank(client.getPhone()) && StringUtils.isNotBlank(cm.getPhone())) {
            client.setPhone(cm.getPhone());
            cm.addResolution("Телефон клиента обновлён");
        }

        if (StringUtils.isBlank(client.getClientName()) && StringUtils.isNotBlank(cm.getClientName())) {
            client.setClientName(cm.getClientName());
            cm.addResolution("Имя клиента обновлено");
        }

        if (client.getRegion() == null && cm.getRegion() != null) {
            client.setRegion(cm.getRegion());
            cm.addResolution("Регион клиента обновлен");
        }

        clientRepository.save(client);
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
        message += "[ROISTATID]: " + clientMessage.getRoistatid() + "\n\n";
        message += "[NAME]: " + clientMessage.getClientName() + "\n\n";
//        message += "[PHONE]: " + "\n\n";
        message += "[EMAIL]: " + clientMessage.getEmail() + "\n\n";
        message += "[REGION]: " + (clientMessage.getRegion() == null ? "" : clientMessage.getRegion().getMailPrefix()) + "\n\n";
        message += "[REGION_RU]: " + (clientMessage.getRegion() == null ? "" : clientMessage.getRegion().getName()) + "\n\n";

        message += "[BODY]: " + clientMessage.getMessageBody() + "\n\n";

        EmailOut emailOut = new EmailOut(message);
        return emailOut;
    }

}
