package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;

public interface ProvisioningRequestService {
    ProvisioningRequest createProvisioningRequest(String employeeId);
    void transitionTo(Long requestId, ProvisioningRequestStatus status);
    void transitionTo(Long requestId, ProvisioningRequestStatus status, String entraObjectId);
}
