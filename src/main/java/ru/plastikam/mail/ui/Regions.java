package ru.plastikam.mail.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.plastikam.mail.model.Region;
import ru.plastikam.mail.repository.RegionRepository;

import java.io.Serializable;
import java.util.List;

@Service
@Scope(scopeName = "view")
public class Regions implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Regions.class);

    @Autowired
    RegionRepository regionRepository;

    private List<Region> regions;

    public Regions() {

    }

    public List<Region> getClients() {
        if (regions == null) {
            regions = regionRepository.findAll();
        }
        return regions;
    }

}
