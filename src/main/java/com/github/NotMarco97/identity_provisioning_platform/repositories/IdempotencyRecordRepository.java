package com.github.NotMarco97.identity_provisioning_platform.repositories;

import com.github.NotMarco97.identity_provisioning_platform.entities.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, Long> {
    IdempotencyRecord findByKey(String key);
    Boolean existsByKey(String key);
}
