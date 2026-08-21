package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.entities.AuditEvent;
import com.github.NotMarco97.identity_provisioning_platform.repositories.AuditEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditEventServiceImp implements AuditEventService {
    private final AuditEventRepository auditEventRepository;

    public AuditEventServiceImp(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @Override
    public void recordEvent(AuditEvent auditEvent) {
        auditEventRepository.save(auditEvent);
    }

    @Override
    public List<AuditEvent> findByRequestId(Long requestId) {
        return auditEventRepository.findByRequestId(requestId);
    }

    @Override
    public List<AuditEvent> findByTargetEmployee(String targetEmployee) {
        return auditEventRepository.findByTargetEmployee(targetEmployee);
    }
}