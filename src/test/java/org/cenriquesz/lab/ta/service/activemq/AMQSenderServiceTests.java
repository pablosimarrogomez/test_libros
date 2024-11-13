package org.cenriquesz.lab.ta.service.activemq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AMQSenderServiceTests {

  @Autowired
  private AMQSenderService service;

  @Test
  public void testSend() {
    service.send("test-message");
  }

}
