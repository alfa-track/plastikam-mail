package ru.plastikam.mail.misc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.plastikam.mail.model.Client;
import ru.plastikam.mail.receiver.AbstractService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class ImportContacts extends AbstractService {

    @Transactional
    public void doImport() {

        try (Stream<String> stream = Files.lines(Paths.get("./Контакты - Контакты.csv"))) {

            stream.skip(1).forEach(this::processLine);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void processLine(String line) {
        try {
            if (StringUtils.isBlank(line))
                return;

            String[] split = line.split(",", 2);
            if (split.length != 2) {
                logger.error("line " + line);
            }

            Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(split[0]);

            clientRepository.save(new Client(split[1], "", date, "Гуглотабличка", null));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
