package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.graph.GraphCreateUserRequest;

public interface GraphUserRequestService {
    GraphCreateUserRequest buildRequest(String employeeId);
}
