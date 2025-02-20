package ru.haskov.manager.service.impl;

import lombok.RequiredArgsConstructor;
import ru.haskov.manager.model.entity.CrackHashData;
import ru.haskov.manager.model.PasswordData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.haskov.common.dto.HashDTO;
import ru.haskov.common.dto.WorkerResponseDTO;
import ru.haskov.manager.repository.ManagerRepository;
import ru.haskov.manager.service.ManagerService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MongoManagerService implements ManagerService {
    private final ManagerRepository managerRepository;

    @Override
    public ResponseEntity<UUID> crackHashRequest(HashDTO hashDTO) {
        UUID uuid = UUID.randomUUID();
        managerRepository.save(new CrackHashData(uuid, "IN PROGRESS", new ArrayList<>()));



        return ResponseEntity.ok(uuid);
    }

    @Override
    public ResponseEntity<PasswordData> crackHackResponse(UUID requestId) {
        CrackHashData data = managerRepository.findById(requestId).orElse(null);
        if (data == null) {
            return null;
        }
        return ResponseEntity.ok(new PasswordData(data.getStatus(), data.getPasswords()));
    }

    @Override
    public ResponseEntity<String> receiveWorkerResult(WorkerResponseDTO responseDTO) {
        CrackHashData data = managerRepository.findById(responseDTO.getTaskId()).orElse(null);
        if (data == null) {
            return ResponseEntity.badRequest().build();
        }
        List<String> newPasswords = new ArrayList<>(data.getPasswords());
        newPasswords.addAll(responseDTO.getData());

        managerRepository.save(new CrackHashData(responseDTO.getTaskId(), "IN PROGRESS",
                newPasswords));

        return ResponseEntity.ok().build();
    }
}
