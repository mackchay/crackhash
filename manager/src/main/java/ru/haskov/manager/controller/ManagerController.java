package ru.haskov.manager.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.haskov.common.dto.HashDTO;
import model.PasswordData;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.manager.service.ManagerService;

@RestController
@AllArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/api/hash/crack")
    public ResponseEntity<String> hackRequest(@RequestBody HashDTO hashDTO) {
        return managerService.hackRequest(hashDTO);
    }

    @GetMapping("/api/hash/status")
    public PasswordData hackResponse(@RequestParam String requestId) {
        return managerService.hackResponse(requestId);
    }

    @PatchMapping("/internal/api/manager/hash/crack/request")
    public ResponseEntity<String> receiveWorkerResult(@RequestBody WorkerResponseDTO responseDTO) {
        managerService.receiveWorkerResult(responseDTO);
        return ResponseEntity.ok("Result received");
    }
}
