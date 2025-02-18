package ru.haskov.worker.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.haskov.common.dto.WorkerTaskDTO;
import ru.haskov.worker.service.WorkerService;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class WorkerController {
    private WorkerService workerService;

    @PostMapping("/internal/api/manager/hash/crack/task")
    public ResponseEntity<String> task(@RequestBody WorkerTaskDTO workerTaskDTO) {
        workerService.task(workerTaskDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}
