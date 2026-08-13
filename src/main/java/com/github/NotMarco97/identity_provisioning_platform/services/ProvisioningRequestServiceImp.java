package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.repositories.ProvisioningRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class ProvisioningRequestServiceImp implements ProvisioningRequestService {
    private final ProvisioningRequestRepository provisioningRequestRepository;

    public ProvisioningRequestServiceImp(ProvisioningRequestRepository provisioningRequestRepository) {
        this.provisioningRequestRepository = provisioningRequestRepository;
    }

    @Override
    public ProvisioningRequest createProvisioningRequest(String employeeId) {
        return null;
    }

    @Override
    public void transitionTo(Long requestId, ProvisioningRequestStatus status) {

    }
}
