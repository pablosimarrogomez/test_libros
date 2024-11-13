package org.cenriquesz.lab.ta.configuration;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedAsyncClient;
import com.amazon.sqs.javamessaging.ExtendedAsyncClientConfiguration;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AwsConfig {

  private static final String DEFAULT_DOMAIN = "amazonaws.com";

  @Value("${aws.credentials.access-key}")
  private String accessKey;

  @Value("${aws.credentials.secret-key}")
  private String secretKey;

  @Value("${aws.config.region}")
  private String region;

  @Value("${aws.config.domain:}")
  private String domain;

  @Value("${aws.sqs.bucket}")
  private String sqsBucket;

  @Bean
  public AwsCredentialsProvider awsCredentialsProvider() {
    return StaticCredentialsProvider
        .create(AwsBasicCredentials
            .create(accessKey, secretKey));
  }

  @Bean
  public S3AsyncClient s3Client(AwsCredentialsProvider awsCredentials) {
    return S3AsyncClient.builder()
        .region(Region.of(region))
        .endpointOverride(createEndpoint(S3AsyncClient.SERVICE_METADATA_ID))
        .credentialsProvider(awsCredentials)
        .serviceConfiguration(S3Configuration.builder().build())
        .build();
  }

  @Bean
  public SqsAsyncClient sqsClient(AwsCredentialsProvider awsCredentials, S3AsyncClient s3Client) {
    SqsAsyncClient sqsClient = SqsAsyncClient.builder()
        .region(Region.of(region))
        .endpointOverride(createEndpoint(SqsAsyncClient.SERVICE_METADATA_ID))
        .credentialsProvider(awsCredentials)
        .build();
    ExtendedAsyncClientConfiguration extendedClientConfig = new ExtendedAsyncClientConfiguration()
        .withPayloadSupportEnabled(s3Client, sqsBucket);
    return new AmazonSQSExtendedAsyncClient(sqsClient, extendedClientConfig);
  }

  @Bean
  public SesClient sesClient(AwsCredentialsProvider awsCredentials) {
    return SesClient.builder()
        .region(Region.of(region))
        .endpointOverride(createEndpoint(SesClient.SERVICE_METADATA_ID))
        .credentialsProvider(awsCredentials)
        .build();
  }

  private URI createEndpoint(String serviceName) {
    if (domain == null || domain.isBlank() || domain.equals(DEFAULT_DOMAIN)) {
      return null;
    }
    return URI.create(String.format("https://%s.%s.%s", serviceName, region, domain));
  }
}