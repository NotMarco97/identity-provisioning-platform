package com.github.NotMarco97.identity_provisioning_platform.repositories;

import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvisioningRequestRepository extends JpaRepository<ProvisioningRequest, Long> {
    List<ProvisioningRequest> findByEmployeeIdAndStatusNotIn(String employeeId, List<ProvisioningRequestStatus> statuses);
}
