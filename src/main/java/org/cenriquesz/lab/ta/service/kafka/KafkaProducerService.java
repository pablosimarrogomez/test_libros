package org.cenriquesz.lab.ta.service.kafka;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  @Qualifier("kafkaRetryTemplate")
  private RetryTemplate retryTemplate;

  public void sendMessage(String topic, String message) {
    String messageId = UUID.randomUUID().toString();
    try {
      retryTemplate.execute(retryContext -> {
        int attempt = retryContext.getRetryCount() + 1;
        try {
          // Envío del mensaje a Kafka
          SendResult<String, String> a = kafkaTemplate
              .send(topic, messageId, message)
              .get(30, TimeUnit.SECONDS);

          a.getProducerRecord();

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
          // Lanzamos excepción para activar el mecanismo de reintento
          throw new RuntimeException("Error al enviar el mensaje a Kafka", e);
        }
        return null;
      });
    } catch (Exception e) {
      // TODO - Añadir logs con la excepcion
      e.getMessage();
    }
  }
}
