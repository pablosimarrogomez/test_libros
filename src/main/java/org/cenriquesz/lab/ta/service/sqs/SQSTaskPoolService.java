package org.cenriquesz.lab.ta.service.sqs;

import io.awspring.cloud.sqs.listener.MessageListenerContainer;
import io.awspring.cloud.sqs.listener.DefaultListenerContainerRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.cenriquesz.lab.ta.configuration.sqs.SQSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class SQSTaskPoolService {

  private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();;

  @Autowired
  @Qualifier(SQSConfig.LISTENER_QUEUE_TASK_SCHEDULER)
  private ThreadPoolTaskScheduler sqsQueuesTaskScheduler;

  @Autowired
  private DefaultListenerContainerRegistry defaultListenerContainerRegistry;

  private static boolean taskExecutorQueueFull(ThreadPoolTaskExecutor executor) {
    return executor.getQueueSize() > executor.getQueueCapacity() - 10;
  }

  public void checkListenerTaskPool(String listenerContainerId, ThreadPoolTaskExecutor taskExecutor) {
    if (taskExecutorQueueFull(taskExecutor)) {
      MessageListenerContainer<?> listenerContainer =
          defaultListenerContainerRegistry.getContainerById(listenerContainerId);
      if (listenerContainer != null && listenerContainer.isRunning()) {
        listenerContainer.stop();
        createListenerContainerRestartTask(taskExecutor, listenerContainer);
      }
    }
  }

  private void createListenerContainerRestartTask(
      ThreadPoolTaskExecutor taskExecutor, MessageListenerContainer<?> listenerContainer) {}

}
