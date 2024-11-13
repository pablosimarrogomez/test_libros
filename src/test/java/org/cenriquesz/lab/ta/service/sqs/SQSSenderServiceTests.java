package org.cenriquesz.lab.ta.service.sqs;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import org.cenriquesz.lab.orq.util.Utils;

@SpringBootTest
public class SQSSenderServiceTests {

  @Autowired
  private SQSListenerService listenerService;

  @Autowired
  private SQSSenderService service;

  @Value("${sqs.queues.test-01}")
  private String test01QueueName;

  @Value("${sqs.queues.test-02}")
  private String test02QueueName;

  @Test
  public void send() {
    CompletableFuture<SendMessageResponse> resp = service.send(
        test01QueueName, "test-message-01");
    Utils.wait(1);
    boolean sent = resp.isDone();
    Assertions.assertTrue(sent);
  }

  @Test
  public void sendLargeMessage() {
    // Generate a string larger than 256KB. Here, we're generating a string of size 300KB.
    CompletableFuture<SendMessageResponse> resp = service.send(
        test01QueueName, String.format("test-message: %s", "*".repeat(307200)));
    Utils.wait(1);
    boolean sent = resp.isDone();
    Assertions.assertTrue(sent);
  }

  @Test
  public void sendAvalanche() {
    listenerService.processMessage01("AAA");

    int limit = 1;
    for(int i = 0; i < limit; i++) {
      service.send(test01QueueName, String.format("test-message-%02d", i));
    }
    Utils.wait(600);
  }

}
