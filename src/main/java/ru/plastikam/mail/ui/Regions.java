package ru.plastikam.mail.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.Region;
import ru.plastikam.mail.repository.AbstractRepository;
import ru.plastikam.mail.repository.RegionRepository;

@Service
@Scope(scopeName = "view")
public class Regions extends InMemoryTableBacking<Region> {

    @Autowired
    RegionRepository regionRepository;

    public Regions() {

    }

    @Override
    protected AbstractRepository<Region> getRepository() {
        return regionRepository;
    }

}
