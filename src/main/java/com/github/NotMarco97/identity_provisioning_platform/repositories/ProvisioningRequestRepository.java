package com.github.NotMarco97.identity_provisioning_platform.repositories;

import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvisioningRequestRepository extends JpaRepository<ProvisioningRequest, Long> {

}
