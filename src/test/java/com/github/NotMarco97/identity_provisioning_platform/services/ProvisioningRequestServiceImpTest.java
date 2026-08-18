package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.repositories.ProvisioningRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProvisioningRequestServiceImpTest {
    @Mock
    private ProvisioningRequestRepository provisioningRequestRepository;

    @MockitoBean
    private ProvisioningRequestService provisioningRequestService;

    @Test
    void createProvisioningRequestIsValid() {

    }

    @Test
    void transitionTo() {
    }
}