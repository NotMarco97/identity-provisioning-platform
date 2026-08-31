package com.github.NotMarco97.identity_provisioning_platform.dto;

public class PasswordProfile {
    private boolean forceChangePasswordNextSignIn;
    private String password;

    public boolean getForceChangePasswordNextSignIn() {
        return forceChangePasswordNextSignIn;
    }
    public void setForceChangePasswordNextSignIn(boolean forceChangePasswordNextSignIn) {
        this.forceChangePasswordNextSignIn = forceChangePasswordNextSignIn;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
