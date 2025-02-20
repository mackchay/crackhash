package ru.haskov.manager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.haskov.manager.model.entity.CrackHashData;

import java.util.UUID;

@Repository
public interface ManagerRepository extends MongoRepository<CrackHashData, UUID> {

}
