package ru.plastikam.mail.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DatabasePatches databasePatches;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //dictionaryFill.fill();
        //dictionaryFill.addNewRegion();
//        databasePatches.patchUpdatedDate();

    }
}
