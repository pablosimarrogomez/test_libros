package org.cenriquesz.lab.ta.service.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaProducerServiceTests {

  @Autowired
  private KafkaProducerService service;

  @Test
  public void testSend() {
    String topic = "test-topic";
    String message = "test-message";
    service.sendMessage(topic, message);
  }

}
