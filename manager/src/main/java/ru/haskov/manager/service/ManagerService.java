package ru.haskov.manager.service;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.haskov.common.dto.HashDTO;
import ru.haskov.common.dto.ResponseDTO;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.common.dto.WorkerTaskDTO;
import ru.haskov.manager.properties.WorkerProperties;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ManagerService {

    @Value("/internal/api/manager/hash/crack/task")
    private String taskRequestAPI;

    @Value("${WORKERS:1}")
    private int workerCount;

    @NonNull
    private final WebClient.Builder webClientBuilder;

    private final Map<String, ResponseDTO> taskStatusMap = new ConcurrentHashMap<>();

    @NonNull
    private final WorkerProperties properties;

    public ResponseEntity<String> hackRequest(HashDTO hashDTO) {
        String taskId = UUID.randomUUID().toString();
        List<String> workerUrls = properties.getUrls();

        taskStatusMap.put(taskId, new ResponseDTO("IN PROGRESS", null));

        for (int i = 0; i < workerCount; i++) {
            webClientBuilder.build()
                    .post()
                    .uri(workerUrls.get(i) + taskRequestAPI)
                    .bodyValue(new WorkerTaskDTO(hashDTO, taskId, i, workerCount))
                    .retrieve().toBodilessEntity().timeout(Duration.ofSeconds(60)).
                    subscribe(
                        response -> System.out.println("Task send successfully"),
                        error -> {
                            if (taskStatusMap.get(taskId).getStatus().equals("IN PROGRESS")) {
                                taskStatusMap.put(taskId, new ResponseDTO("ERROR", null));
                            }
                        }
            );
        }

        return ResponseEntity.ok(taskId);
    }

    public ResponseDTO hackResponse(String requestId) {
        return taskStatusMap.get(requestId);
    }

    public ResponseEntity<Void> receiveWorkerResult(WorkerResponseDTO responseDTO) {
        System.out.println("Received worker result: " + responseDTO.getTaskId()
                + ":" + responseDTO.getData());
        if (!responseDTO.getData().isEmpty() &&
                taskStatusMap.get(responseDTO.getTaskId()).getStatus().equals("IN PROGRESS")) {
            taskStatusMap.put(responseDTO.getTaskId(),
                    new ResponseDTO("READY", responseDTO.getData()));
        }
        return ResponseEntity.ok().build();
    }
}
