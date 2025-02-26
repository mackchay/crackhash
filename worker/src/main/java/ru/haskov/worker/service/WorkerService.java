package ru.haskov.worker.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import ru.haskov.common.dto.WorkerTaskDTO;

public interface WorkerService {

    void task(WorkerTaskDTO workerTaskDTO, Channel channel, long deliveryTag);
}
