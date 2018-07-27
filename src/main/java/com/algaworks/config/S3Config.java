package com.algaworks.config;

import com.algaworks.config.property.AlgamoneyApiProperty;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class S3Config {

    @Autowired
    private AlgamoneyApiProperty property;

    @Bean
    public AmazonS3 amazonS3() throws Exception {
        AWSCredentials credentials = new BasicAWSCredentials(
                property.getS3().getAccessKeyId(), property.getS3().getSecretAccessKey());

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        return amazonS3;
    }

}
