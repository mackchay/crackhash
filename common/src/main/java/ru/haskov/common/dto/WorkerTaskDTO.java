package ru.haskov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkerTaskDTO {
    private HashDTO hashDTO;
    private String taskID;
    private int partNumber;
    private int partCount;
}
