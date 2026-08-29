package com.github.NotMarco97.identity_provisioning_platform.Oauth;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GraphProperties.class)
public class GraphAuthConfig {

    @Bean
    public ConfidentialClientApplication confidentialClientApplication(GraphProperties props) throws Exception {
        return ConfidentialClientApplication.builder(
                props.getClientId(),
                ClientCredentialFactory.createFromSecret(props.getClientSecret()))
                .authority("https://login.microsoftonline.com/" + props.getTenantId() + "/") .build();

    }

}
