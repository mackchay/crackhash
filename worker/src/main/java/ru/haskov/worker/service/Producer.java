package ru.haskov.worker.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.haskov.common.dto.WorkerResponseDTO;

@Service
@Setter
@RequiredArgsConstructor
public class Producer {

    @Value("${queue.task_response}")
    private String taskQueueName;

    @NonNull
    private RabbitTemplate amqpTemplate;

    public void sendTaskResponse(WorkerResponseDTO workerResponseDTO) {
        amqpTemplate.convertAndSend(taskQueueName, workerResponseDTO);
    }

}
