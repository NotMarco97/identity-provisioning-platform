package com.github.NotMarco97.identity_provisioning_platform.exceptions;

public class PartialFailureException extends RuntimeException{
    public PartialFailureException(String message){
        super(message);
    }
}
