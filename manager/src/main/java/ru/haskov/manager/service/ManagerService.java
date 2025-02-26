package ru.haskov.manager.service;

import ru.haskov.manager.model.PasswordData;
import org.springframework.http.ResponseEntity;
import ru.haskov.common.dto.HashDTO;
import ru.haskov.common.dto.WorkerResponseDTO;

import java.util.UUID;

public interface ManagerService {

    ResponseEntity<UUID> crackHashRequest(HashDTO hashDTO);

    ResponseEntity<PasswordData> crackHackResponse(UUID requestId);

    void receiveWorkerResult(WorkerResponseDTO responseDTO);
}
