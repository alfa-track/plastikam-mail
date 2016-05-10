package ru.plastikam.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.plastikam.mail.configuration.ConfigureJSF;

@EnableScheduling
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {""})
@EnableAutoConfiguration
public class EmailApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[]{EmailApplication.class, ConfigureJSF.class}, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EmailApplication.class);
    }
}
