package com.algaworks.config;

import com.algaworks.config.property.AlgamoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySources({
        @PropertySource(value = { "file:///${USERPROFILE}/configuration/.mail.properties" }, ignoreResourceNotFound = true)
})
public class MailConfig {

    @Autowired
    private AlgamoneyApiProperty algamoney;

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender javaMailSender() {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.connectiontimeout", 10000);

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setJavaMailProperties(properties);

        javaMailSender.setHost(env.getProperty("mail-host"));
        javaMailSender.setPort(Integer.parseInt(env.getProperty("mail-port")));
        javaMailSender.setUsername(env.getProperty("mail-username"));
        javaMailSender.setPassword(env.getProperty("mail-password"));

        return javaMailSender;
    }

}
