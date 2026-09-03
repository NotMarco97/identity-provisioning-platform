package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.Oauth.GraphTokenService;
import com.github.NotMarco97.identity_provisioning_platform.graph.GraphCreateUserRequest;
import com.github.NotMarco97.identity_provisioning_platform.graph.GraphUserListResponse;
import com.github.NotMarco97.identity_provisioning_platform.entities.GraphUser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GraphServiceImp implements GraphService {
    private final GraphTokenService graphTokenService;
    private final GraphUserRequestService graphUserRequestService;
    private final RestClient restClient;

    public GraphServiceImp(GraphTokenService graphTokenService, GraphUserRequestService graphUserRequestService, RestClient restClient){
        this.graphTokenService = graphTokenService;
        this.graphUserRequestService= graphUserRequestService;
        this.restClient = restClient;
    }

    @Override
    public boolean userPrincipalNameExists(String userPrincipalName) {
        String filter = "userPrincipalName eq '" + userPrincipalName + "'";
        GraphUserListResponse response = restClient.get()
                .uri("https://graph.microsoft.com/v1.0/users?$filter=" + filter)
                .header("Authorization", "Bearer " + graphTokenService.getAccessToken())
                .retrieve()
                .body(GraphUserListResponse.class);

        return response != null && !response.getValue().isEmpty();
    }

    @Override
    public GraphUser createUser(String employeeId) {
        GraphCreateUserRequest request = graphUserRequestService.buildRequest(employeeId);
        return restClient.post()
                .uri("https://graph.microsoft.com/v1.0/users")
                .header("Authorization", "Bearer " + graphTokenService.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(GraphUser.class);
    }
}
