package com.github.NotMarco97.identity_provisioning_platform.graph;

import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlan;

public interface GraphProvider {
    void createUser(String employeeId, ProvisioningPlan provisioningPlan);

}
