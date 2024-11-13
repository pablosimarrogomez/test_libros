package org.cenriquesz.lab.ta.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class KafkaConfig {

  // Número máximo de intentos
  @Value("${kafka.retry.max-attempts}")
  private int maxAttempts;

  // Intervalo inicial en milisegundos
  @Value("${kafka.retry.initial-interval}")
  private long initialInterval;

  // Intervalo máximo en milisegundos
  @Value("${kafka.retry.max-interval}")
  private long maxInterval;

  // Factor de crecimiento del intervalo
  @Value("${kafka.retry.multiplier}")
  private double multiplier;

  @Bean(name = "kafkaRetryTemplate")
  public RetryTemplate retryTemplate() {
    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(maxAttempts);

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(initialInterval);
    backOffPolicy.setMaxInterval(maxInterval);
    backOffPolicy.setMultiplier(multiplier);

    RetryTemplate retryTemplate = new RetryTemplate();
    retryTemplate.setBackOffPolicy(backOffPolicy);
    retryTemplate.setRetryPolicy(retryPolicy);

    return retryTemplate;
  }
}
