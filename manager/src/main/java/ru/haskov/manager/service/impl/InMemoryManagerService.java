package ru.haskov.manager.service.impl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.haskov.common.dto.HashDTO;
import ru.haskov.manager.model.PasswordData;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.common.dto.WorkerTaskDTO;
import ru.haskov.manager.properties.WorkerProperties;
import ru.haskov.manager.service.ManagerService;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InMemoryManagerService implements ManagerService {

    @Value("/internal/api/manager/hash/crack/task")
    private String taskRequestAPI;

    @Value("${WORKERS:1}")
    private int workerCount;

    @NonNull
    private final WebClient.Builder webClientBuilder;

    private final Map<UUID, PasswordData> taskStatusMap = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> workerSuccessMap = new ConcurrentHashMap<>();

    @NonNull
    private final WorkerProperties properties;

    public ResponseEntity<UUID> crackHashRequest(HashDTO hashDTO) {
        UUID taskId = UUID.randomUUID();
        List<String> workerUrls = properties.getUrls();

        taskStatusMap.put(taskId, new PasswordData("IN PROGRESS", new ArrayList<>()));
        workerSuccessMap.put(taskId, 0);

        for (int i = 0; i < workerCount; i++) {
            webClientBuilder.build()
                    .post()
                    .uri(workerUrls.get(i) + taskRequestAPI)
                    .bodyValue(new WorkerTaskDTO(hashDTO, taskId, i, workerCount))
                    .retrieve().toBodilessEntity().timeout(Duration.ofSeconds(600)).
                    subscribe(
                        response -> {
                            System.out.println("Task send successfully");
                        },
                        error -> {
                            if (taskStatusMap.get(taskId).getStatus().equals("IN PROGRESS")) {
                                taskStatusMap.put(taskId, new PasswordData("ERROR",
                                        taskStatusMap.get(taskId).getData())
                                );
                            }
                        }
            );
        }

        return ResponseEntity.ok(taskId);
    }

    public ResponseEntity<PasswordData> crackHackResponse(UUID requestId) {
        return ResponseEntity.ok(taskStatusMap.get(requestId));
    }

    public void receiveWorkerResult(WorkerResponseDTO responseDTO) {
        System.out.println("Received worker result: " + responseDTO.getTaskId()
                + ":" + responseDTO.getData());
        if (taskStatusMap.get(responseDTO.getTaskId()).getStatus().equals("IN PROGRESS")) {
            List<String> newData = new ArrayList<>();
            newData.addAll(taskStatusMap.get(responseDTO.getTaskId()).getData());
            newData.addAll(responseDTO.getData());
            String status = "IN PROGRESS";
            if (workerSuccessMap.get(responseDTO.getTaskId()) == workerCount - 1) {
                status = "READY";
            }
            taskStatusMap.put(
                    responseDTO.getTaskId(),
                    new PasswordData(status, newData)
            );
            workerSuccessMap.put(
                    responseDTO.getTaskId(),
                    workerSuccessMap.get(responseDTO.getTaskId()) + 1
            );

        }
    }
}
