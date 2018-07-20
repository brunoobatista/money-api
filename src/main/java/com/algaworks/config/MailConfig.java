package com.algaworks.config;

import com.algaworks.config.property.AlgamoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Autowired
    private AlgamoneyApiProperty algamoney;

    @Bean
    public JavaMailSender javaMailSender() {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.starttls", true);
        properties.put("mail.smtp.connectiontimeout", 10000);

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setJavaMailProperties(properties);

        javaMailSender.setHost(algamoney.getMail().getHost());
        javaMailSender.setPort(algamoney.getMail().getPort());
        javaMailSender.setUsername(algamoney.getMail().getUsername());
        javaMailSender.setPassword(algamoney.getMail().getPassword());

        return javaMailSender;
    }

}
