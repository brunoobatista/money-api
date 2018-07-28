package com.algaworks.config;

import com.algaworks.config.property.AlgamoneyApiProperty;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@Configuration
@PropertySources({
        @PropertySource(value = { "file:///${USERPROFILE}/configuration/.s3-aws.properties" }, ignoreResourceNotFound = true)
})
public class S3Config {

    @Autowired
    private AlgamoneyApiProperty property;

    @Autowired
    private Environment env;

    @Bean
    public AmazonS3 amazonS3() throws Exception {

        AWSCredentials credentials = new BasicAWSCredentials(
                env.getProperty("access-key-id"), env.getProperty("secret-access-key"));

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.SA_EAST_1)
                .build();

        if (!amazonS3.doesBucketExistV2(property.getS3().getBucket())) {
            amazonS3.createBucket(new CreateBucketRequest(property.getS3().getBucket()));
            BucketLifecycleConfiguration.Rule regras =
                    new BucketLifecycleConfiguration.Rule()
                    .withId("Regra de expiração de arquivos temporários.")
                    .withFilter(new LifecycleFilter(
                                new LifecycleTagPredicate(
                                new Tag("expirar", "true"))))
                    .withExpirationInDays(1)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);

            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(regras);

            amazonS3.setBucketLifecycleConfiguration(
                    property.getS3().getBucket(),
                    configuration);
        }
        return amazonS3;
    }

}
