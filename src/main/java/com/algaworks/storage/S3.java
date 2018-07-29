package com.algaworks.storage;

import com.algaworks.config.property.AlgamoneyApiProperty;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Component
public class S3 {

    private static final Logger logger = LoggerFactory.getLogger(S3.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AlgamoneyApiProperty property;

    public void salvar(String objeto) {
        SetObjectTaggingRequest setObjectTaggingRequest =
                new SetObjectTaggingRequest(
                        property.getS3().getBucket(),
                        objeto,
                        new ObjectTagging(Collections.emptyList())
                );
        amazonS3.setObjectTagging(setObjectTaggingRequest);
    }

    public String salvarTemporariamente(MultipartFile arquivo) {
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(arquivo.getContentType());
        objectMetadata.setContentLength(arquivo.getSize());

        String nomeUnico = gerarNomeUnico(arquivo.getOriginalFilename());
        try {
            PutObjectRequest put = new PutObjectRequest(
                    property.getS3().getBucket(),
                    nomeUnico,
                    arquivo.getInputStream(),
                    objectMetadata
            ).withAccessControlList(acl);

            put.setTagging(new ObjectTagging(
                    Arrays.asList(new Tag("expirar", "true"))
            ));

            amazonS3.putObject(put);
            if (logger.isDebugEnabled()) {
                logger.debug("Arquivo enviado com sucesso para o S3", arquivo.getOriginalFilename());
            }
        } catch (IOException e) {
            throw new RuntimeException("Problemas ao enviar arquivo para o S3.", e);
        }
        return nomeUnico;
    }

    public void remover(String objeto) {
        DeleteObjectRequest delete = new DeleteObjectRequest(
                property.getS3().getBucket(),
                objeto
        );

        amazonS3.deleteObject(delete);
    }

    public void substituir(String objetoAntigo, String objetoNovo) {
        if (StringUtils.hasText(objetoAntigo)) {
            this.remover(objetoAntigo);
        }
        this.salvar(objetoNovo);
    }

    public String configurarUrl(String objeto) {
        return "\\\\"+property.getS3().getBucket()+".s3.amazonaws.com/"+objeto;
    }

    private String gerarNomeUnico(String originalFileName) {
        return UUID.randomUUID().toString()+"_"+originalFileName;
    }

}
