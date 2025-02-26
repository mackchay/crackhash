package ru.haskov.worker.service;

import ru.haskov.common.dto.WorkerTaskDTO;

public interface WorkerService {
    void task(WorkerTaskDTO workerTaskDTO);
}
