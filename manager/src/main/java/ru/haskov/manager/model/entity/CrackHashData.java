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
@lombok.NonNull
@AllArgsConstructor
public class CrackHashData {

    @Id
    private UUID id;

    @NonNull
    private String status;
    private List<String> passwords;
}
