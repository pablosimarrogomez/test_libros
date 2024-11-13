package org.cenriquesz.lab.ta.configuration.sqs;

import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableAsync
@Configuration
public class SQSConfig {

  public static final int TOTAL_QUEUES = 2;
  public static final String TASK_POOL_QUEUE_1 = "task-pool-queue-01";
  public static final String TASK_POOL_QUEUE_2 = "task-pool-queue-02";
  public static final String LISTENER_CONTAINER_QUEUE_1 = "listener-container-queue-01";
  public static final String LISTENER_CONTAINER_QUEUE_2 = "listener-container-queue-02";
  public static final String LISTENER_TASK_CONFIG_QUEUE_1 = "listener-task-config-queue_1";
  public static final String LISTENER_TASK_CONFIG_QUEUE_2 = "listener-task-config-queue_2";

  public static final String LISTENER_TASK_CONFIG_MAP = "listener-task-config-map";
  public static final String LISTENER_QUEUE_TASK_SCHEDULER = "listener-queue-task-scheduler";

  @Value("${sqs.queues.test-01}")
  private String test01QueueName;

  @Value("${sqs.queues.test-02}")
  private String test02QueueName;


  @Bean(name = TASK_POOL_QUEUE_1)
  public ThreadPoolTaskExecutor tes01QueueTaskExecutor() {
    return createTaskPool(TASK_POOL_QUEUE_1);
  }

  @Bean(name = TASK_POOL_QUEUE_2)
  public ThreadPoolTaskExecutor tes02QueueTaskExecutor() {
    return createTaskPool(TASK_POOL_QUEUE_2);
  }

  @Bean(name = LISTENER_QUEUE_TASK_SCHEDULER)
  public ThreadPoolTaskScheduler sqsListenerTaskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(TOTAL_QUEUES);
    scheduler.setThreadNamePrefix("scheduled-pool-sqs-queues-");
    scheduler.initialize();
    return scheduler;
  }

  private ThreadPoolTaskExecutor createTaskPool(String poolName) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(15);
    executor.setThreadNamePrefix(String.format("%s-", poolName));
    executor.initialize();
    return executor;
  }

  @lombok.Builder
  @lombok.Value
  public static class SQSListenerTaskPoolConfig {

    String queueName;
    SqsMessageListenerContainer<?> listenerContainer;
    ThreadPoolTaskExecutor taskPoolExecutor;
  }
}
