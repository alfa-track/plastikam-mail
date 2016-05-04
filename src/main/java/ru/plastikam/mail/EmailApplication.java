package ru.plastikam.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EmailApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EmailApplication.class, args);
    }
}
