package ru.haskov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkerResponseDTO {
    private String taskId;
    private String data;
}
