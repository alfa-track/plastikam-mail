package ru.plastikam.mail.repository;

import org.springframework.stereotype.Repository;
import ru.plastikam.mail.model.Region;

@Repository
public interface RegionRepository extends AbstractRepository<Region> {

    Region findOneByMailPrefix(String mailPrefix);

}
