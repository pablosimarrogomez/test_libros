package org.cenriquesz.lab.ta.service.sqs;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.cenriquesz.lab.orq.util.Utils;
import org.cenriquesz.lab.ta.configuration.sqs.SQSConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class SQSListenerService {

  private static final Logger logger = LoggerFactory.getLogger(SQSListenerService.class);

  @Autowired SQSTaskPoolService sqsTaskPoolService;

  @Autowired
  @Qualifier(SQSConfig.TASK_POOL_QUEUE_1)
  ThreadPoolTaskExecutor taskExecutorQueue01;

  @Autowired
  @Qualifier(SQSConfig.TASK_POOL_QUEUE_2)
  ThreadPoolTaskExecutor taskExecutorQueue02;

  @SqsListener(value = "${sqs.queues.test-01}", id = SQSConfig.LISTENER_CONTAINER_QUEUE_1, maxConcurrentMessages = "1", maxMessagesPerPoll="1")
  public void listen01(Message<?> message) {
    logger.info("Mensaje recibido: {}", message.getPayload());

    logger.info("Comprobacion de los pool de tareas: {}", message.getPayload());
    sqsTaskPoolService.checkListenerTaskPool(
        SQSConfig.LISTENER_CONTAINER_QUEUE_1, taskExecutorQueue01);

    logger.info("Se procesa el mensaje: {}", message.getPayload());
    processMessage01(message);
  }

  @SqsListener(value = "${sqs.queues.test-02}", id = SQSConfig.LISTENER_CONTAINER_QUEUE_2, maxConcurrentMessages = "1", maxMessagesPerPoll="1")
  public void listen02(Message<?> message) {
    processMessage02(message);
  }

  @Async(SQSConfig.TASK_POOL_QUEUE_1)
  public void processMessage01(String message){
    logger.info("Mensaje recibido por el procesador 01: {}", message);
    Utils.wait(15);
    logger.info("Mensaje procesado por el procesador 01: {}", message);
  }

  @Async(SQSConfig.TASK_POOL_QUEUE_1)
  protected void processMessage01(Message<?> message) {
    logger.info("Mensaje recibido por el procesador 01: {}", message.getPayload());
    processMessage("SQS-01", message);
  }

  @Async(SQSConfig.TASK_POOL_QUEUE_2)
  protected void processMessage02(Message<?> message) {
    processMessage("SQS-02", message);
  }

  private void processMessage(String listenerName, Message<?> message) {
    logger.info("{} received message: {}", listenerName, message.getPayload());
    Utils.wait(10);
    logger.info("{} processed message: {}", listenerName, message.getPayload());
  }
}
