package ru.haskov.manager.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.haskov.common.dto.HashDTO;
import ru.haskov.manager.model.PasswordData;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.manager.service.ManagerService;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/api/hash/crack")
    public ResponseEntity<UUID> hackRequest(@RequestBody HashDTO hashDTO) {
        return managerService.crackHashRequest(hashDTO);
    }

    @GetMapping("/api/hash/status")
    public ResponseEntity<PasswordData> hackResponse(@RequestParam UUID requestId) {
        return managerService.crackHackResponse(requestId);
    }

    @PatchMapping("/internal/api/manager/hash/crack/request")
    public ResponseEntity<String> receiveWorkerResult(@RequestBody WorkerResponseDTO responseDTO) {
        return managerService.receiveWorkerResult(responseDTO);
    }
}
