package ru.haskov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class WorkerResponseDTO {
    private UUID taskId;
    private List<String> data;
}
