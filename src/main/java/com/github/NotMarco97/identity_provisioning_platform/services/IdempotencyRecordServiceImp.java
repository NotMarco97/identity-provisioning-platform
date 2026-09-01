package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.entities.IdempotencyRecord;
import com.github.NotMarco97.identity_provisioning_platform.entities.IdempotencyRecordStatus;
import com.github.NotMarco97.identity_provisioning_platform.repositories.IdempotencyRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdempotencyRecordServiceImp implements IdempotencyRecordService {
    IdempotencyRecordRepository idempotencyRecordRepository;
    public IdempotencyRecordServiceImp(IdempotencyRecordRepository idempotencyRecordRepository) {
        this.idempotencyRecordRepository = idempotencyRecordRepository;
    }

    @Override
    public Optional<String> checkForExistingKey(String key) {
        if(!idempotencyRecordRepository.existsByKey(key)) {
            return Optional.empty();
        }

        IdempotencyRecord idempotencyRecord =  idempotencyRecordRepository.findByKey(key);

        if (idempotencyRecord.getStatus() != IdempotencyRecordStatus.COMPLETED){
            return Optional.empty();
        }

        return Optional.of(idempotencyRecord.getResponseBody());
    }

    @Override
    public void markInProgress(String key) {
        IdempotencyRecord idempotencyRecord = idempotencyRecordRepository.findByKey(key);
        if (idempotencyRecord == null) {
            idempotencyRecord = new IdempotencyRecord();
            idempotencyRecord.setKey(key);
        }
        idempotencyRecord.setStatus(IdempotencyRecordStatus.IN_PROGRESS);
        idempotencyRecordRepository.save(idempotencyRecord);
    }

    @Override
    public void markCompleted(String key, String bodyResponse) {
        IdempotencyRecord idempotencyRecord = idempotencyRecordRepository.findByKey(key);
        idempotencyRecord.setStatus(IdempotencyRecordStatus.COMPLETED);
        idempotencyRecord.setResponseBody(bodyResponse);
        idempotencyRecordRepository.save(idempotencyRecord);
    }
}
