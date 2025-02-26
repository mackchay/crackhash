package ru.haskov.worker.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.common.dto.WorkerTaskDTO;
import ru.haskov.worker.model.BruteForce;
import ru.haskov.worker.service.Producer;
import ru.haskov.worker.service.WorkerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NormalWorkerService implements WorkerService {
    @NonNull
    private Producer producer;

    @NonNull
    private BruteForce bruteForce;

    @Override
    @RabbitListener(queues = ("${queue.task_request}"))
    public void task(@Payload WorkerTaskDTO workerTaskDTO) {
        System.out.println(workerTaskDTO);
        List<String> password = bruteForce.hack(workerTaskDTO.getHashDTO().getHash(),
                workerTaskDTO.getHashDTO().getMaxLength(),
                workerTaskDTO.getPartNumber(),
                workerTaskDTO.getPartCount());
        System.out.println(password);
        producer.sendTaskResponse(new WorkerResponseDTO(workerTaskDTO.getTaskID(), password));
    }
}
