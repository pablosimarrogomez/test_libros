package org.cenriquesz.lab.ta.configuration;

import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class AMQConfig {

  @Value("${spring.activemq.broker-url}")
  private String brokerUrl;

  @Value("${spring.activemq.ssl.key-store}")
  private String keyStore;

  @Value("${spring.activemq.ssl.key-store-password}")
  private String keyStorePassword;

  @Value("${spring.activemq.ssl.trust-store}")
  private String trustStore;

  @Value("${spring.activemq.ssl.trust-store-password}")
  private String trustStorePassword;

  @Bean
  @Primary
  public ActiveMQSslConnectionFactory connectionFactory() throws Exception {
    ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory();
    connectionFactory.setBrokerURL(brokerUrl);
    connectionFactory.setKeyStore(keyStore);
    connectionFactory.setKeyStorePassword(keyStorePassword);
    connectionFactory.setTrustStore(trustStore);
    connectionFactory.setTrustStorePassword(trustStorePassword);
    return connectionFactory;
  }
}
