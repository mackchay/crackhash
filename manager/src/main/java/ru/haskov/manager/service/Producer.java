package ru.haskov.manager.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.haskov.common.dto.WorkerTaskDTO;

@Component
@RequiredArgsConstructor
public class Producer {
    @Value("${queue.task_request}")
    String queueName;

    @NonNull
    private AmqpTemplate amqpTemplate;

    public void sendTask(WorkerTaskDTO workerTaskDTO) {

        amqpTemplate.convertAndSend(queueName, workerTaskDTO, message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
    }
}
