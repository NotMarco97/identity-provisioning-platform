package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.entities.AuditEvent;
import com.github.NotMarco97.identity_provisioning_platform.entities.Employee;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.repositories.ProvisioningRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProvisioningRequestServiceImpTest {
    @Mock
    private ProvisioningRequestRepository provisioningRequestRepository;
    @Mock
    private AuditEventService auditEventServiceImp;

    @InjectMocks
    private ProvisioningRequestServiceImp provisioningRequestServiceImp;

    @Test
    void createProvisioningInvalidStatusShouldReturn409() {
        ProvisioningRequest provisioningRequest = new ProvisioningRequest();
        provisioningRequest.setEmployeeId("EMP-0001");
        provisioningRequest.setStatus(ProvisioningRequestStatus.PENDING);

        when(provisioningRequestRepository.findByEmployeeIdAndStatusNotIn("EMP-0001", List.of(ProvisioningRequestStatus.COMPLETED, ProvisioningRequestStatus.FAILED)))
                .thenReturn(List.of(provisioningRequest));

        assertThrows(IllegalStateException.class, () -> provisioningRequestServiceImp.createProvisioningRequest("EMP-0001"));
    }

    @Test
    void CreateProvisioningShouldSucceed(){
        when(provisioningRequestRepository.findByEmployeeIdAndStatusNotIn("EMP-0001",
                List.of(ProvisioningRequestStatus.COMPLETED, ProvisioningRequestStatus.FAILED)))
                .thenReturn(List.of());

        doAnswer(invocation-> {ProvisioningRequest ProvisioningRequestARG = invocation.getArgument(0);
            ProvisioningRequestARG.setId(1L);
            ProvisioningRequestARG.setUpdatedAt(LocalDateTime.now());
            ProvisioningRequestARG.setCreatedAt(LocalDateTime.now());
            return ProvisioningRequestARG;}).when(provisioningRequestRepository).save(any(ProvisioningRequest.class));

        ProvisioningRequest result =  provisioningRequestServiceImp.createProvisioningRequest("EMP-0001");

        assertEquals(ProvisioningRequestStatus.RECEIVED, result.getStatus());
    }

    @Test
    void transitionToInvalidStatusShouldReturn409_WhenIdNotFound() {

        when(provisioningRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> provisioningRequestServiceImp.transitionTo(1L, ProvisioningRequestStatus.PENDING));
    }

    @Test
    void transitionToInvalidStatusShould409_WhenTransactionNotAllowed() {
        ProvisioningRequest request = new ProvisioningRequest();
        request.setStatus(ProvisioningRequestStatus.PENDING);
        when(provisioningRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(IllegalStateException.class, () -> provisioningRequestServiceImp.transitionTo(1L, ProvisioningRequestStatus.RECEIVED));

    }

    @Test
    void transitionToShouldSucceed(){
        ProvisioningRequest request = new ProvisioningRequest();
        request.setStatus(ProvisioningRequestStatus.PLANNED);

        when(provisioningRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        provisioningRequestServiceImp.transitionTo(1L, ProvisioningRequestStatus.PENDING);

        assertEquals(ProvisioningRequestStatus.PENDING,  request.getStatus());

    }

    @Test
    void createProvisioningRequestShouldRecordAuditEvent_OnSuccess() {
        when(provisioningRequestRepository.findByEmployeeIdAndStatusNotIn("EMP-0001",
                List.of(ProvisioningRequestStatus.COMPLETED, ProvisioningRequestStatus.FAILED)))
                .thenReturn(List.of());

        doAnswer(invocation-> {ProvisioningRequest ProvisioningRequestARG = invocation.getArgument(0);
            ProvisioningRequestARG.setId(1L);
            ProvisioningRequestARG.setUpdatedAt(LocalDateTime.now());
            ProvisioningRequestARG.setCreatedAt(LocalDateTime.now());
            return ProvisioningRequestARG;}).when(provisioningRequestRepository).save(any(ProvisioningRequest.class));

        provisioningRequestServiceImp.createProvisioningRequest("EMP-0001");

        verify(auditEventServiceImp).recordEvent(any(AuditEvent.class));
    }

    @Test
    void createProvisioningRequest409ShouldRecordAuditEvent_OnInvalidEmployeeId() {
        ProvisioningRequest provisioningRequest = new ProvisioningRequest();
        provisioningRequest.setEmployeeId("EMP-0001");
        provisioningRequest.setStatus(ProvisioningRequestStatus.PENDING);

        when(provisioningRequestRepository.findByEmployeeIdAndStatusNotIn("EMP-0001", List.of(ProvisioningRequestStatus.COMPLETED, ProvisioningRequestStatus.FAILED)))
                .thenReturn(List.of(provisioningRequest));

        assertThrows(IllegalStateException.class,
                () -> provisioningRequestServiceImp.createProvisioningRequest("EMP-0001"));

        verify(auditEventServiceImp).recordEvent(any(AuditEvent.class));

    }

    @Test
    void transitionToSuccessShouldRecordAuditEvent(){
        ProvisioningRequest request = new ProvisioningRequest();
        request.setStatus(ProvisioningRequestStatus.PLANNED);

        when(provisioningRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        provisioningRequestServiceImp.transitionTo(1L, ProvisioningRequestStatus.PENDING);

        verify(auditEventServiceImp).recordEvent(any(AuditEvent.class));
    }

    @Test
    void transitionToInvalidStatus409_WhenTransactionNotAllowed_ShouldRecordAuditEvent() {
        ProvisioningRequest request = new ProvisioningRequest();
        request.setStatus(ProvisioningRequestStatus.PENDING);
        when(provisioningRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(IllegalStateException.class,
                () -> provisioningRequestServiceImp.transitionTo(1L, ProvisioningRequestStatus.RECEIVED));

        verify(auditEventServiceImp).recordEvent(any(AuditEvent.class));
    }
}