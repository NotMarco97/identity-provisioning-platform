package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.Oauth.GraphTokenService;
import com.github.NotMarco97.identity_provisioning_platform.entities.GraphUser;
import org.springframework.web.client.RestClient;

public interface GraphService {
    boolean userPrincipalNameExists(String userPrincipalName);
    GraphUser createUser(String employeeId);
}
