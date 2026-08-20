package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.entities.AuditEvent;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.repositories.AuditEventRepository;
import com.github.NotMarco97.identity_provisioning_platform.repositories.ProvisioningRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProvisioningRequestServiceImp implements ProvisioningRequestService {
    private final ProvisioningRequestRepository provisioningRequestRepository;
    private final AuditEventServiceImp auditEventServiceImp;

    public ProvisioningRequestServiceImp(ProvisioningRequestRepository provisioningRequestRepository, AuditEventServiceImp auditEventServiceImp) {
        this.provisioningRequestRepository = provisioningRequestRepository;
        this.auditEventServiceImp = auditEventServiceImp;
    }

    private final Map<ProvisioningRequestStatus, Set<ProvisioningRequestStatus>> legalTransaction = Map.of(
            ProvisioningRequestStatus.RECEIVED, Set.of(ProvisioningRequestStatus.VALIDATED, ProvisioningRequestStatus.FAILED),
            ProvisioningRequestStatus.VALIDATED, Set.of(ProvisioningRequestStatus.PLANNED, ProvisioningRequestStatus.FAILED),
            ProvisioningRequestStatus.PLANNED, Set.of(ProvisioningRequestStatus.PENDING, ProvisioningRequestStatus.FAILED),
            ProvisioningRequestStatus.PENDING, Set.of(ProvisioningRequestStatus.COMPLETED, ProvisioningRequestStatus.FAILED));

    @Override
    public ProvisioningRequest createProvisioningRequest(String employeeId) {
        List<ProvisioningRequest> activeRequest = provisioningRequestRepository.findByEmployeeIdAndStatusNotIn(employeeId,
                                                    List.of(ProvisioningRequestStatus.COMPLETED, ProvisioningRequestStatus.FAILED));

        if(!activeRequest.isEmpty()){
            AuditEvent auditEvent = new AuditEvent();
            auditEvent.setActor("System");
            auditEvent.setOutcome("FAILED");
            auditEvent.setStatusChange("REQUEST_CREATION_BLOCKED");
            auditEvent.setTargetEmployee(employeeId);
            auditEventServiceImp.recordEvent(auditEvent);
            throw new IllegalStateException("An active provisioning request already exists.");
        }

        ProvisioningRequest provisioningRequest = new ProvisioningRequest();
        provisioningRequest.setEmployeeId(employeeId);
        provisioningRequest.setStatus(ProvisioningRequestStatus.RECEIVED);

        ProvisioningRequest savedRequest = provisioningRequestRepository.save(provisioningRequest);
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setActor("System");
        auditEvent.setOutcome("SUCCESS");
        auditEvent.setRequestId(provisioningRequest.getId());
        auditEvent.setStatusChange("REQUEST_CREATED");
        auditEvent.setTargetEmployee(savedRequest.getEmployeeId());
        auditEventServiceImp.recordEvent(auditEvent);

        return savedRequest;
    }

    @Override
    public void transitionTo(Long requestId, ProvisioningRequestStatus status) {
        ProvisioningRequest provisioningRequest = provisioningRequestRepository.findById(requestId).orElseThrow();

            if(!legalTransaction.getOrDefault(provisioningRequest.getStatus(), Set.of()).contains(status)){
                AuditEvent auditEvent = new AuditEvent();
                auditEvent.setActor("System");
                auditEvent.setRequestId(provisioningRequest.getId());
                auditEvent.setTargetEmployee(provisioningRequest.getEmployeeId());
                auditEvent.setStatusChange("STATUS_CHANGED_TO_FAILED");
                auditEvent.setOutcome("FAILED");
                auditEventServiceImp.recordEvent(auditEvent);
                throw new IllegalStateException();
            }

            provisioningRequest.setStatus(status);
            provisioningRequestRepository.save(provisioningRequest);

        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setActor("System");
        auditEvent.setOutcome("SUCCESS");
        auditEvent.setRequestId(provisioningRequest.getId());
        auditEvent.setStatusChange("REQUEST_CHANGED_TO_" + provisioningRequest.getStatus().toString());
        auditEvent.setTargetEmployee(provisioningRequest.getEmployeeId());
        auditEventServiceImp.recordEvent(auditEvent);
    }
}
