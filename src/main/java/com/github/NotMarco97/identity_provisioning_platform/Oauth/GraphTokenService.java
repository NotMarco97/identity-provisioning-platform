package com.github.NotMarco97.identity_provisioning_platform.Oauth;


import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.MsalServiceException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Service
public class GraphTokenService {
    private static final Logger logger = LoggerFactory.getLogger(GraphTokenService.class.getName());
    private final ConfidentialClientApplication app;
    public GraphTokenService(ConfidentialClientApplication app) {
        this.app = app;
    }

    public String getAccessToken() throws Exception {
        ClientCredentialParameters params = ClientCredentialParameters.builder(
                Collections.singleton("https://graph.microsoft.com/.default"))
                .build();
        try {
            IAuthenticationResult result = app.acquireToken(params).get();
            logger.info("Graph token acquired. correlationId={}", app.correlationId());
            return result.accessToken();

        } catch (ExecutionException | InterruptedException e) {
            Throwable cause = e.getCause();
            if (cause instanceof MsalServiceException msalEx) {
                logger.error("Graph token acquisition failed. correlationId={}, error={}",
                        msalEx.getCause(), msalEx.getMessage());
            } else {
                logger.error("Graph token acquisition failed with unexpected cause. correlationId={}", app.correlationId(), e);
            }
            throw new GraphAuthenticationException("Failed to acquire Graph access token", e);
        }

    }
}
