package ru.haskov.manager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.handler.annotation.Payload;
import ru.haskov.common.dto.WorkerTaskDTO;
import ru.haskov.manager.model.entity.CrackHashData;
import ru.haskov.manager.model.PasswordData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.haskov.common.dto.HashDTO;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.manager.repository.ManagerRepository;
import ru.haskov.manager.service.ManagerService;
import ru.haskov.manager.service.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class MongoManagerService implements ManagerService {
    @Value("${workers_num}")
    private Integer workers;
    @NonNull
    private final ManagerRepository managerRepository;

    @NonNull
    private final Producer producer;

    @Override
    public ResponseEntity<UUID> crackHashRequest(HashDTO hashDTO) {
        UUID uuid = UUID.randomUUID();
        managerRepository.save(new CrackHashData(uuid, hashDTO.getHash(), hashDTO.getMaxLength(),
                "IN PROGRESS", new ArrayList<>(), 0));
        for (int i = 0; i < workers; i++) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = null;
            try {
                jsonMessage = objectMapper.writeValueAsString(new WorkerTaskDTO(hashDTO, uuid, i, workers));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Serialized message: " + jsonMessage);

            producer.sendTask(new WorkerTaskDTO(hashDTO, uuid, i, workers));
        }

        return ResponseEntity.ok(uuid);
    }

    @Override
    public ResponseEntity<PasswordData> crackHackResponse(UUID requestId) {
        CrackHashData data = managerRepository.findById(requestId).orElse(null);
        if (data == null) {
            return null;
        }
        return ResponseEntity.ok(new PasswordData(data.getStatus(), data.getPasswords()));
    }

    @Override
    @RabbitListener(queues = ("${queue.task_response}"))
    public void receiveWorkerResult(@Payload @NonNull WorkerResponseDTO responseDTO) {
        CrackHashData data = managerRepository.findById(responseDTO.getTaskId()).orElse(null);
        if (data == null) {
            System.out.println("No data found for task id " + responseDTO.getTaskId());
            return;
        }

        if (!responseDTO.getData().isEmpty()) {
            if (data.getPasswords().contains(responseDTO.getData().get(0))) {
                System.out.println("Duplicate deleted.");
                return;
            }
        }

        List<String> newPasswords = new ArrayList<>(data.getPasswords());
        newPasswords.addAll(responseDTO.getData());
        int partsCount = data.getPartsCount();
        String status = "IN PROGRESS";
        if (partsCount == workers - 1) {
            status = "READY";
        }

        managerRepository.save(new CrackHashData(responseDTO.getTaskId(),
                data.getHash(), data.getMaxLength(), status, newPasswords, partsCount + 1));
    }
}
