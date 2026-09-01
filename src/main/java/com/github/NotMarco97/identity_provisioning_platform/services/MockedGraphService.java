package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.exceptions.AuthorizationFailureException;
import com.github.NotMarco97.identity_provisioning_platform.exceptions.DuplicateIdentityException;
import com.github.NotMarco97.identity_provisioning_platform.exceptions.PartialFailureException;
import com.github.NotMarco97.identity_provisioning_platform.exceptions.ThrottlingException;
import com.github.NotMarco97.identity_provisioning_platform.graph.MockedGraph;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlan;
import org.springframework.stereotype.Service;

@Service
public class MockedGraphService implements MockedGraph {
    @Override
    public void createUser(String employeeId, ProvisioningPlan provisioningPlan) {

        if(employeeId.equals("EMP-THROTTLE")){
            throw new ThrottlingException("Rate limit exceeded");
        }
        if(employeeId.equals("EMP-DUPLICATE")){
            throw new DuplicateIdentityException("User already exists");
        }
        if(employeeId.equals("EMP-PARTIAL")){
            throw new PartialFailureException("Group assignment failed");
        }
        if(employeeId.equals("EMP-UNAUTHORIZED")){
            throw new AuthorizationFailureException("Insufficient permissions");
        }

    }
}
