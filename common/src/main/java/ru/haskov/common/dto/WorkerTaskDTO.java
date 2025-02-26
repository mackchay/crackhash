package ru.haskov.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerTaskDTO implements Serializable {
    @JsonProperty("hashDTO")
    private HashDTO hashDTO;

    @JsonProperty("taskID")
    private UUID taskID;

    @JsonProperty("partNumber")
    private int partNumber;

    @JsonProperty("partCount")
    private int partCount;
}
