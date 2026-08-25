package com.github.NotMarco97.identity_provisioning_platform.exceptions;

public class AuthorizationFailureException extends  RuntimeException {
    public AuthorizationFailureException(String message){
        super(message);
    }
}
