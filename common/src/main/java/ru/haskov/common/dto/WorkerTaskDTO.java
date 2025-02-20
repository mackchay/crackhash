package ru.haskov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class WorkerTaskDTO {
    private HashDTO hashDTO;
    private UUID taskID;
    private int partNumber;
    private int partCount;
}
