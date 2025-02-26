package ru.haskov.worker.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.haskov.common.dto.WorkerTaskDTO;
import ru.haskov.worker.service.impl.SimpleWorkerServiceImpl;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class WorkerController {
    private SimpleWorkerServiceImpl simpleWorkerServiceImpl;

    @PostMapping("/internal/api/manager/hash/crack/task")
    public ResponseEntity<String> task(@RequestBody WorkerTaskDTO workerTaskDTO) {
        simpleWorkerServiceImpl.task(workerTaskDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}
