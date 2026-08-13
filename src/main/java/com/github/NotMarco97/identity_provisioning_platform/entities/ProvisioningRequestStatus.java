package com.github.NotMarco97.identity_provisioning_platform.entities;

public enum ProvisioningRequestStatus {
    RECEIVED,      // request has been created, not yet validated
    VALIDATED,     // employee confirmed to exist
    PLANNED,       // access plan successfully resolved
    PENDING,       // ready for execution
    COMPLETED,     // process finished successfully
    FAILED         // an error occurred during validation or planning
}
