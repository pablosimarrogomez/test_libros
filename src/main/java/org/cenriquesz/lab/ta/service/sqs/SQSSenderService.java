package org.cenriquesz.lab.ta.service.sqs;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SQSSenderService {

  private final SqsAsyncClient sqsClient;

  @Autowired
  public SQSSenderService(SqsAsyncClient sqsClient) {
    this.sqsClient = sqsClient;
  }

  public CompletableFuture<SendMessageResponse> send(String queue, String message) {
    return sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queue).build())
        .thenCompose(getQueueUrlResponse ->
            sendUsingQueueUrl(getQueueUrlResponse.queueUrl(), message));
  }

  public CompletableFuture<SendMessageResponse> sendUsingQueueUrl(String queueUrl, String message) {
    SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
        .queueUrl(queueUrl)
        .messageGroupId("unique-group-id")
        .messageDeduplicationId(UUID.randomUUID().toString())
        .messageBody(message)
        .build();
    return sqsClient.sendMessage(sendMessageRequest);
  }

}
