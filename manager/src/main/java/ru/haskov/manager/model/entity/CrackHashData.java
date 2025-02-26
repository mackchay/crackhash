package ru.haskov.manager.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

@Document
@Getter
@Setter
@AllArgsConstructor
public class CrackHashData {

    @Id
    private UUID id;
    @NonNull
    private String hash;
    @NonNull
    private Integer maxLength;

    @NonNull
    private String status;

    @NonNull
    private List<String> passwords;

    @NonNull
    private Integer partsCount;
}
