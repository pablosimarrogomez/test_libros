package org.cenriquesz.lab.ta.service.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class AMQSenderService {

  @Autowired
  private JmsTemplate jmsTemplate;

  @Value("${amq.queues.test}")
  private String testQueueName;

  public void send(String message) {
    send(testQueueName, message);
  }

  public void send(String queue, String message) {
    jmsTemplate.convertAndSend(queue, message);
  }
}
