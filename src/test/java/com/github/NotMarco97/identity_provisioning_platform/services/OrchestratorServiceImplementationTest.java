package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.entities.EmployeeStatus;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.exceptions.AuthorizationFailureException;
import com.github.NotMarco97.identity_provisioning_platform.exceptions.DuplicateIdentityException;
import com.github.NotMarco97.identity_provisioning_platform.exceptions.PartialFailureException;
import com.github.NotMarco97.identity_provisioning_platform.exceptions.ThrottlingException;
import com.github.NotMarco97.identity_provisioning_platform.graph.GraphProvider;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlan;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlanResolver;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrchestratorServiceImplementationTest {
    @Mock
    private ProvisioningRequestService provisioningRequestServiceImp;
    @Mock
    private EmployeeService employeeServiceImp;
    @Mock
    private ProvisioningPlanResolver provisioningPlanResolver;
    @Mock
    private GraphProvider graphProviderService;

    @InjectMocks
    private OrchestratorServiceImplementation orchestratorServiceImplementation;

    @Test
    void orchestrateSucceeds() {
        List<String> groups = List.of("group1", "group2");
        List<String> licenses = List.of("test", "test");

        ProvisioningRequest fakeRequest = new ProvisioningRequest();
        fakeRequest.setId(1L);
        EmployeeResponse fakeEmployee = new EmployeeResponse();
        fakeEmployee.setEmployeeId("EMP-0001");
        fakeEmployee.setFirstName("John");
        fakeEmployee.setLastName("Doe");
        fakeEmployee.setDepartment("IT");
        fakeEmployee.setJobTitle("Help Desk");
        fakeEmployee.setSalary(40000.0);
        fakeEmployee.setStatus("test");
        fakeEmployee.setEmail("jdoe@company.com");
        fakeEmployee.setCreatedAt("test");
        fakeEmployee.setUpdatedAt("test");

        ProvisioningPlan fakePlan = new ProvisioningPlan(groups, licenses);

        when(provisioningRequestServiceImp.createProvisioningRequest("EMP-0001")).thenReturn(fakeRequest);
        when(employeeServiceImp.findByEmployeeId("EMP-0001")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);
        doNothing().when(graphProviderService).createUser(any(), any());

        orchestratorServiceImplementation.orchestrate("EMP-0001");

        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.VALIDATED);
        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.PLANNED);
        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.PENDING);
        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.COMPLETED);
    }

    @Test
    void orchestrateShouldFailOnThrottling(){
        List<String> groups = List.of("group1", "group2");
        List<String> licenses = List.of("test", "test");

        ProvisioningRequest fakeRequest = new ProvisioningRequest();
        fakeRequest.setId(1L);
        EmployeeResponse fakeEmployee = new EmployeeResponse();
        fakeEmployee.setEmployeeId("EMP-THROTTLE");
        fakeEmployee.setFirstName("John");
        fakeEmployee.setLastName("Doe");
        fakeEmployee.setDepartment("IT");
        fakeEmployee.setJobTitle("Help Desk");
        fakeEmployee.setSalary(40000.0);
        fakeEmployee.setStatus("test");
        fakeEmployee.setEmail("jdoe@company.com");
        fakeEmployee.setCreatedAt("test");
        fakeEmployee.setUpdatedAt("test");

        ProvisioningPlan fakePlan = new ProvisioningPlan(groups, licenses);

        when(provisioningRequestServiceImp.createProvisioningRequest("EMP-THROTTLE")).thenReturn(fakeRequest);
        when(employeeServiceImp.findByEmployeeId("EMP-THROTTLE")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);
        doThrow(new ThrottlingException("Rate limit exceeded")).when(graphProviderService).createUser(any(), any());

        orchestratorServiceImplementation.orchestrate("EMP-THROTTLE");

        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.FAILED);

    }

    @Test
    void orchestrateShouldFailOnAuthorization(){
        List<String> groups = List.of("group1", "group2");
        List<String> licenses = List.of("test", "test");

        ProvisioningRequest fakeRequest = new ProvisioningRequest();
        fakeRequest.setId(1L);
        EmployeeResponse fakeEmployee = new EmployeeResponse();
        fakeEmployee.setEmployeeId("EMP-00001");
        fakeEmployee.setFirstName("John");
        fakeEmployee.setLastName("Doe");
        fakeEmployee.setDepartment("IT");
        fakeEmployee.setJobTitle("Help Desk");
        fakeEmployee.setSalary(40000.0);
        fakeEmployee.setStatus("test");
        fakeEmployee.setEmail("jdoe@company.com");
        fakeEmployee.setCreatedAt("test");
        fakeEmployee.setUpdatedAt("test");

        ProvisioningPlan fakePlan = new ProvisioningPlan(groups, licenses);

        when(provisioningRequestServiceImp.createProvisioningRequest("EMP-00001")).thenReturn(fakeRequest);
        when(employeeServiceImp.findByEmployeeId("EMP-00001")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);
        doThrow(new AuthorizationFailureException("Insufficient permissions")).when(graphProviderService).createUser(any(), any());

        orchestratorServiceImplementation.orchestrate("EMP-00001");

        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.FAILED);

    }

    @Test
    void orchestrateShouldFailOnPartialFailure(){
        List<String> groups = List.of("group1", "group2");
        List<String> licenses = List.of("test", "test");

        ProvisioningRequest fakeRequest = new ProvisioningRequest();
        fakeRequest.setId(1L);
        EmployeeResponse fakeEmployee = new EmployeeResponse();
        fakeEmployee.setEmployeeId("EMP-00001");
        fakeEmployee.setFirstName("John");
        fakeEmployee.setLastName("Doe");
        fakeEmployee.setDepartment("IT");
        fakeEmployee.setJobTitle("Help Desk");
        fakeEmployee.setSalary(40000.0);
        fakeEmployee.setStatus("test");
        fakeEmployee.setEmail("jdoe@company.com");
        fakeEmployee.setCreatedAt("test");
        fakeEmployee.setUpdatedAt("test");

        ProvisioningPlan fakePlan = new ProvisioningPlan(groups, licenses);

        when(provisioningRequestServiceImp.createProvisioningRequest("EMP-00001")).thenReturn(fakeRequest);
        when(employeeServiceImp.findByEmployeeId("EMP-00001")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);
        doThrow(new PartialFailureException("Assignment Failed")).when(graphProviderService).createUser(any(), any());

        orchestratorServiceImplementation.orchestrate("EMP-00001");

        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.FAILED);

    }

    @Test
    void orchestrateShouldFailOnDuplicate(){
        List<String> groups = List.of("group1", "group2");
        List<String> licenses = List.of("test", "test");

        ProvisioningRequest fakeRequest = new ProvisioningRequest();
        fakeRequest.setId(1L);
        EmployeeResponse fakeEmployee = new EmployeeResponse();
        fakeEmployee.setEmployeeId("EMP-00001");
        fakeEmployee.setFirstName("John");
        fakeEmployee.setLastName("Doe");
        fakeEmployee.setDepartment("IT");
        fakeEmployee.setJobTitle("Help Desk");
        fakeEmployee.setSalary(40000.0);
        fakeEmployee.setStatus("test");
        fakeEmployee.setEmail("jdoe@company.com");
        fakeEmployee.setCreatedAt("test");
        fakeEmployee.setUpdatedAt("test");

        ProvisioningPlan fakePlan = new ProvisioningPlan(groups, licenses);

        when(provisioningRequestServiceImp.createProvisioningRequest("EMP-00001")).thenReturn(fakeRequest);
        when(employeeServiceImp.findByEmployeeId("EMP-00001")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);
        doThrow(new DuplicateIdentityException("Duplicate")).when(graphProviderService).createUser(any(), any());

        orchestratorServiceImplementation.orchestrate("EMP-00001");

        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.FAILED);

    }

}