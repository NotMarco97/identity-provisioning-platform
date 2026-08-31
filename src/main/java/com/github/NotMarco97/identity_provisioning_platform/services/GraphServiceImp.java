package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.Oauth.GraphTokenService;
import com.github.NotMarco97.identity_provisioning_platform.dto.GraphUserListResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GraphServiceImp{
    private final GraphTokenService graphTokenService;

    public GraphServiceImp(GraphTokenService graphTokenService){
        this.graphTokenService = graphTokenService;
    }

    public boolean userPrincipalNameExists(String userPrincipalName) {
        String filter = "userPrincipalName eq '" + userPrincipalName + "'";
        RestClient restClient = RestClient.create();
        GraphUserListResponse response = restClient.get()
                .uri("https://graph.microsoft.com/v1.0/users?$filter=" + filter)
                .header("Authorization", "Bearer " + graphTokenService.getAccessToken())
                .retrieve()
                .body(GraphUserListResponse.class);
        return response != null && !response.getValue().isEmpty();
    }

}
