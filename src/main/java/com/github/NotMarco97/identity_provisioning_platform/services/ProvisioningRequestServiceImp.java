package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.repositories.ProvisioningRequestRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProvisioningRequestServiceImp implements ProvisioningRequestService {
    private final ProvisioningRequestRepository provisioningRequestRepository;

    public ProvisioningRequestServiceImp(ProvisioningRequestRepository provisioningRequestRepository) {
        this.provisioningRequestRepository = provisioningRequestRepository;
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
            throw new IllegalStateException("An active provisioning request already exists.");
        }

        ProvisioningRequest provisioningRequest = new ProvisioningRequest();
        provisioningRequest.setEmployeeId(employeeId);
        provisioningRequest.setStatus(ProvisioningRequestStatus.RECEIVED);

        return provisioningRequestRepository.save(provisioningRequest);
    }

    @Override
    public void transitionTo(Long requestId, ProvisioningRequestStatus status) {
            ProvisioningRequest provisioningRequest = provisioningRequestRepository.findById(requestId).orElseThrow();

            if(!legalTransaction.getOrDefault(provisioningRequest.getStatus(), Set.of()).contains(status)){
                throw new IllegalStateException();
            }

            provisioningRequest.setStatus(status);
            provisioningRequestRepository.save(provisioningRequest);
    }
}
