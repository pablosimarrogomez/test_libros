package org.cenriquesz.lab.ta.service.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class AMQReceiverService {

  @JmsListener(destination = "${amq.queues.test}")
  public void receive(String message) {
    System.out.printf("AMQ received message: %s \n", message);
  }
}
