package ru.plastikam.mail.receiver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.configuration.Config;
import ru.plastikam.mail.misc.Util;
import ru.plastikam.mail.model.ClientMessage;
import ru.plastikam.mail.model.EmailIn;
import ru.plastikam.mail.model.Recipient;
import ru.plastikam.mail.model.Region;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailReceiver extends AbstractService {

    public List<EmailIn> read() {

        try {

            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);

            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", Config.Mail.In.username, Config.Mail.In.password);

//            Folder folder = store.getDefaultFolder();


            List<EmailIn> result = new ArrayList<>();

            for (Region r : regionRepository.findAll()) {
                if (!r.isRegion())
                    continue;
                Folder folder = store.getFolder(r.getName());
                folder.open(Folder.READ_WRITE);

                Message messages[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

                int n = messages.length;

                logger.debug("Папка " + r.getName() + " " + n + " новых писем");

                for (int i = 0; i < messages.length && i < Config.Mail.maxMailPerRound; i++) {
                    EmailIn emailIn = new EmailIn();
                    try {
                        emailIn.setDate(messages[i].getReceivedDate());

                        Address[] allRecipients = messages[i].getAllRecipients();

                        if (allRecipients == null) {
                            emailIn.addResolution("У письма нет получателей");
                        } else {
                            for (int j = 0; j < allRecipients.length; j++) {
                                Address recipient = allRecipients[j];
                                if (recipient instanceof InternetAddress)
                                    emailIn.getRecipients().add(new Recipient(((InternetAddress) recipient).getAddress()));
                                else {
                                    emailIn.getRecipients().add(new Recipient(recipient.toString()));
                                }
                            }
                        }

                        InternetAddress from = ((InternetAddress) messages[i].getFrom()[0]);

                        emailIn.setRegion(r);

                        emailIn.setSender(from.getAddress());
                        emailIn.setSenderName(from.getPersonal());
                        emailIn.setSubject(messages[i].getSubject());
                        emailIn.setMessageBody("" + messages[i].getContent().toString());
                        emailIn.setMessageBody("" + getTextFromMessage(messages[i]));
                    } catch (Exception e) {
                        logger.error("Получение письма", e);
                        emailIn.setError(e);
                    }
                    emailInRepository.save(emailIn);
                    result.add(emailIn);

//                messages[i].get
//                messages[i].setFlag(new Flags.Flag() );

                    messages[i].setFlag(Flags.Flag.SEEN, true);
                }

                folder.close(true);

            }

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

    @Autowired
    MessageResender messageResender;

    public void processMail() {

        logger.info("Проверка почты ... ");

        List<EmailIn> emailIns = read();

        for (EmailIn emailIn : emailIns) {

            ClientMessage clientMessage = toClientMessage(emailIn);

            clientMessageRepository.save(clientMessage);

            emailIn.setClientMessage(clientMessage);

            emailInRepository.save(emailIn);

            if (emailIn.isError()) {

            } else {
                messageResender.process(clientMessage);
            }
        }

    }

    public ClientMessage toClientMessage(EmailIn emailIn) {

        ClientMessage cm = new ClientMessage();

        cm.setDate(emailIn.getDate());
        cm.setMessageBody(emailIn.getMessageBody());
        cm.setRegion(emailIn.getRegion());
        cm.setTicket(((long) (Math.random() * Long.MAX_VALUE)));

        if (emailIn.getRegion().getMailPrefix().equals("recover")) {
            // Новое письмо с сайта
            // Новый формат отменён
            String body = emailIn.getMessageBody().replaceAll("\\[", "\n[");
            cm.setEmail(Util.rget(body, "\\[EMAIL\\]:(.*)"));
            cm.setClientName(Util.rget(body, "\\[NAME\\]:(.*)"));
            cm.setSource("Заявка с сайта (Новая)");
            cm.setPhone(Util.rget(body, "\\[PHONE\\]:(.*)"));
            cm.setRoistatid(Util.rget(body, "\\[ROISTATID\\]:(.*)"));
            cm.setMessageBody(body);
            try {
                cm.setTicket(Long.parseLong(Util.rget(body, "\\[TICKET\\]:(.*)")));
            } catch (Exception e) {
                logger.warn("Long.parseLong( [TICKET] )");
            }
        } else if (StringUtils.equalsIgnoreCase("noreply@megagroup.ru", emailIn.getSender())) {
            // Старое с сайта
            // Рабочий формат
            cm.setEmail(Util.rget(emailIn.getMessageBody(), "Ваш email:(.*)", "Ваш e-mail:(.*)", "E-mail:(.*)"));
            cm.setClientName(Util.rget(emailIn.getMessageBody(), "Ваше имя:(.*)", "Название организации или Ваше имя:(.*)", "Как к Вам обращаться.:(.*)"));
            cm.setSource("С сайта");
            cm.setPhone(Util.rget(emailIn.getMessageBody(), "Ваш телефон:(.*)", "Контактный телефон:(.*)"));
            cm.setRoistatid(Util.rget(emailIn.getMessageBody(), "\\[ROISTATID\\]:(.*)"));
        } else {
            // Письмо с простой и корпоративной почты
            cm.setEmail(emailIn.getSender());
            cm.setClientName(emailIn.getSenderName());

            cm.setRoistatid(emailIn.parseRoistatid());
            cm.setSource("Письмо на регион");
            cm.setPhone("");
        }

        return cm;
    }

}
