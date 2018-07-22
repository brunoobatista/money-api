package com.algaworks.mail;

import com.algaworks.model.Lancamento;
import com.algaworks.model.Usuario;
import com.algaworks.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine thymeleaf;

    /*@Autowired
    private LancamentoRepository repo;
    @EventListener
    private void teste(ApplicationReadyEvent event) {
        String template = "mail/aviso-lancamentos-vencidos";

        List<Lancamento> li = repo.findAll();
        Map<String, Object> variaveis = new HashMap<>();

        variaveis.put("lancamentos", li);

        this.enviarEmail("brunoliveirabatista@gmail.com", Arrays.asList("brunorafael.o@hotmail.com"),
                "Testando", template, variaveis);
    }*/

    public void avisarSobreLancamentosVencidos(List<Lancamento> vencidos,
                                               List<Usuario> destinatarios) {
        Map<String, Object> variaveis = new HashMap<>();
        variaveis.put("lancamentos", vencidos);
        List<String> emails = destinatarios.stream()
                .map(u -> u.getEmail())
                .collect(Collectors.toList());
        this.enviarEmail("brunoliveirabatista@gmail.com",
                emails,
                "Lançamentos Vencidos",
                "mail/aviso-lancamentos-vencidos",
                variaveis);
    }

    public void enviarEmail(String remetente, List<String> destinatarios,
                            String assunto, String template, Map<String, Object> variaveis) {
        Context context = new Context(new Locale("pt", "BR"));
        variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

        String mensagem = thymeleaf.process(template, context);

        this.enviarEmail(remetente, destinatarios, assunto, mensagem);
    }

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
