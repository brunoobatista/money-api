package com.algaworks.repository.listener;

import com.algaworks.AlgamoneyApiApplication;
import com.algaworks.model.Lancamento;
import com.algaworks.storage.S3;
import org.springframework.util.StringUtils;

import javax.persistence.PostLoad;

public class LancamentoAnexoListener {

    @PostLoad
    public void postLoad(Lancamento lancamento) {
        if (StringUtils.hasText(lancamento.getAnexo())) {
            S3 s3 = AlgamoneyApiApplication.getBean(S3.class);
            lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
        }
    }

}
