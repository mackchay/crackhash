package ru.haskov.worker.service;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.common.dto.WorkerTaskDTO;
import ru.haskov.worker.model.BruteForce;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Service
@RequiredArgsConstructor
public class WorkerService {
    @NonNull
    private BruteForce bruteForce;
    @NonNull
    private WebClient.Builder webClientBuilder;

    @Value("${manager.url}")
    private String managerUrl;

    @Value("${server.port}")
    private String port;

    public void task(WorkerTaskDTO workerTaskDTO) {
        String password = bruteForce.hack(workerTaskDTO.getHashDTO().getHash(),
                workerTaskDTO.getHashDTO().getMaxLength(),
                workerTaskDTO.getPartNumber(),
                workerTaskDTO.getPartCount());

        webClientBuilder.build()
                .patch()
                .uri(managerUrl + "/internal/api/manager/hash/crack/request")
                .bodyValue(new WorkerResponseDTO(workerTaskDTO.getTaskID(), password))
                .retrieve().toBodilessEntity()
                .subscribe(
                        response -> System.out.println("Result send successfully!"),
                        error -> System.err.println("Failed sending data to manager: " + error.getMessage())
                );
    }

}
