package com.github.NotMarco97.identity_provisioning_platform.services;

import java.util.Optional;

public interface IdempotencyRecordService {
    Optional<String> checkForExistingKey(String key);
    void markInProgress(String key);
    void markCompleted(String key, String bodyResponse);
}
