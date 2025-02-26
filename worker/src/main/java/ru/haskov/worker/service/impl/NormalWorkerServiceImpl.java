package ru.haskov.worker.service.impl;

import com.rabbitmq.client.Channel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.common.dto.WorkerTaskDTO;
import ru.haskov.worker.model.BruteForce;
import ru.haskov.worker.service.Producer;
import ru.haskov.worker.service.WorkerService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NormalWorkerServiceImpl implements WorkerService {
    @NonNull
    private Producer producer;

    @NonNull
    private BruteForce bruteForce;

    @Override
    @RabbitListener(queues = ("${queue.task_request}"), ackMode = "MANUAL")
    public void task(@Payload WorkerTaskDTO workerTaskDTO, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        System.out.println(workerTaskDTO);
        List<String> password = bruteForce.hack(workerTaskDTO.getHashDTO().getHash(),
                workerTaskDTO.getHashDTO().getMaxLength(),
                workerTaskDTO.getPartNumber(),
                workerTaskDTO.getPartCount());
        System.out.println(password);
        producer.sendTaskResponse(new WorkerResponseDTO(workerTaskDTO.getTaskID(), password));
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            System.out.println("Task failed " + e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
