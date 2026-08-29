package com.github.NotMarco97.identity_provisioning_platform.Oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "graph")
public class GraphProperties {
    private String clientId;
    private String clientSecret;
    private String tenantId;

    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getClientSecret() {
        return clientSecret;
    }
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
