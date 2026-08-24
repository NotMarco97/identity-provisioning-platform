package com.github.NotMarco97.identity_provisioning_platform.exceptions;

public class ThrottlingException extends RuntimeException {
    public ThrottlingException (String message){
        super(message);
    }
}
