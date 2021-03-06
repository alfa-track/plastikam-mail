package ru.plastikam.mail.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.plastikam.mail.model.AbstractEntity;
import ru.plastikam.mail.model.Region;
import ru.plastikam.mail.receiver.AbstractService;
import ru.plastikam.mail.repository.AbstractRepository;

import java.util.List;

@Service
public class DatabasePatches extends AbstractService {

    @Autowired
    ImportContacts importContacts;

    @Transactional
    public void fill() {
        if (regionRepository.count() > 0)
            return;

        regionRepository.save(new Region("almaty", "Алматы"));
        regionRepository.save(new Region("astana", "Астана"));
        regionRepository.save(new Region("voronezh", "Воронеж"));
        regionRepository.save(new Region("ekb", "Екатеринбург"));
        regionRepository.save(new Region("kzn", "Казань"));
        regionRepository.save(new Region("krsk", "Красноярск"));
        regionRepository.save(new Region("nnov", "Нижний Новгород"));
        regionRepository.save(new Region("nsk", "Новосибирск"));
        regionRepository.save(new Region("perm", "Пермь"));
        regionRepository.save(new Region("rostov", "Ростов"));
        regionRepository.save(new Region("samara", "Самара"));
        regionRepository.save(new Region("spb", "Санкт-Петербург"));
        regionRepository.save(new Region("tlt", "Тольятти"));
        regionRepository.save(new Region("ufa", "Уфа"));
        regionRepository.save(new Region("chel", "Челябинск"));
        regionRepository.save(new Region("msk", "msk@plastikam.ru"));

        regionRepository.save(new Region("logistic", "Логистика", false));
        regionRepository.save(new Region("ok", "Отдел Кадров", false));
        regionRepository.save(new Region("ok.plastikam", "Отдел Кадров Mail.ru", false));
        regionRepository.save(new Region("recover", "Заявки с сайта (с мая 2106)", false));

        importContacts.doImport();
    }

    @Autowired
    List<AbstractRepository> repositoryList;

    public void patchUpdatedDate() {
        for (AbstractRepository<AbstractEntity> abstractRepository : repositoryList) {
            logger.info("patchUpdatedDate " + abstractRepository.toString());
            abstractRepository.findAll().
                    forEach(e -> {
                                System.out.print("`");
                                if (e.getUpdateDate() == null) {
                                    e.setUpdateDate(e.getCreateDate());
                                    abstractRepository.save(e);
                                }
                            }
                    );
            System.out.println();
        }
    }
}
