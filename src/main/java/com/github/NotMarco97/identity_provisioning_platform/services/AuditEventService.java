package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.entities.AuditEvent;

import java.util.List;
import java.util.Optional;

public interface AuditEventService {
    void recordEvent(AuditEvent auditEvent);
    List<AuditEvent> findByRequestId(Long requestId);
    List<AuditEvent> findByTargetEmployee(String targetEmployee);
}
