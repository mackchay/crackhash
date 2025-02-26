package ru.haskov.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class HashDTO implements Serializable {
    @JsonProperty("hash")
    private String hash;

    @JsonProperty("maxLength")
    private Integer maxLength;
}
