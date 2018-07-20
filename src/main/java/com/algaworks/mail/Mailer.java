package com.algaworks.mail;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
//import java.util.Arrays;
import java.util.List;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    /*@EventListener
    private void teste(ApplicationReadyEvent event) {
        this.enviarEmail("brunoliveirabatista@gmail.com", Arrays.asList("brunorafael.o@hotmail.com"),
                "Testando", "Olá <br/>Teste <strong>OK</strong>");
    }*/

    public void enviarEmail(String remetente, List<String> destinatarios,
                            String assunto, String mensagem) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF8");
            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
            helper.setSubject(assunto);
            helper.setText(mensagem, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Problemas com o envio de e-mail", e);
        }
    }

}
