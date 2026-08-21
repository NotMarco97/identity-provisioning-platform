package com.github.NotMarco97.identity_provisioning_platform.repositories;

import com.github.NotMarco97.identity_provisioning_platform.entities.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findByRequestId(Long requestId);
    List<AuditEvent> findByTargetEmployee(String targetEmployee);
}
