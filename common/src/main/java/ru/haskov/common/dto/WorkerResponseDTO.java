package ru.haskov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class WorkerResponseDTO {
    @NonNull
    private UUID taskId;
    @NonNull
    private List<String> data;
}
