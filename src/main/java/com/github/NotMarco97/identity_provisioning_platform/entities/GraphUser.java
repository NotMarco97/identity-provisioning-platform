package com.github.NotMarco97.identity_provisioning_platform.entities;
public class GraphUser {
    private String id;
    private String userPrincipalName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }
    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }
}
