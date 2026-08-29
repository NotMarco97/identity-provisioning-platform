package com.github.NotMarco97.identity_provisioning_platform.Oauth;


import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GraphTokenService {
    private final ConfidentialClientApplication app;
    public GraphTokenService(ConfidentialClientApplication app) {
        this.app = app;
    }

    public String getAccessToken() throws Exception {
        ClientCredentialParameters params = ClientCredentialParameters.builder(
                Collections.singleton("https://graph.microsoft.com/.default"))
                .build();
        IAuthenticationResult result = app.acquireToken(params).get();
        return result.accessToken();

    }
}
