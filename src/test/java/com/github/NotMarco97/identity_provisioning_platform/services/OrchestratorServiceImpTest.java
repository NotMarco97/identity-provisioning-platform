package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.entities.GraphUser;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlan;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlanResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrchestratorServiceImpTest {
    @Mock
    private ProvisioningRequestService provisioningRequestService;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private ProvisioningPlanResolver provisioningPlanResolver;
    @Mock
    private GraphService graphService;

    @InjectMocks
    private OrchestratorServiceImp orchestratorServiceImp;

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
        fakeEmployee.setUserPrincipalName("jdoe@mamarcoadamegmail.onmicrosoft.com");
        fakeEmployee.setCreatedAt("test");
        fakeEmployee.setUpdatedAt("test");

        GraphUser fakeCreatedUser = new GraphUser();
        fakeCreatedUser.setId("fake-entra-object-id-123");
        fakeCreatedUser.setUserPrincipalName("jdoe@mamarcoadamegmail.onmicrosoft.com");

        ProvisioningPlan fakePlan = new ProvisioningPlan(groups, licenses);

        when(provisioningRequestService.createProvisioningRequest("EMP-0001")).thenReturn(fakeRequest);
        when(employeeService.findByEmployeeId("EMP-0001")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);
        when(graphService.createUser("EMP-0001")).thenReturn(fakeCreatedUser);

        orchestratorServiceImp.orchestrate("EMP-0001");

        verify(provisioningRequestService).transitionTo(1L, ProvisioningRequestStatus.VALIDATED);
        verify(provisioningRequestService).transitionTo(1L, ProvisioningRequestStatus.PLANNED);
        verify(provisioningRequestService).transitionTo(1L, ProvisioningRequestStatus.PENDING);
        verify(provisioningRequestService).transitionTo(1L, ProvisioningRequestStatus.COMPLETED, "fake-entra-object-id-123");
        verify(employeeService).addEmloyeeEntraObjectId("EMP-0001", "fake-entra-object-id-123");
    }

    @Test
    void orchestrateReturnsCleanly_whenCompletedTransitionLosesRace() {
        List<String> groups = List.of("group1", "group2");
        List<String> licenses = List.of("test", "test");

        ProvisioningRequest fakeRequest = new ProvisioningRequest();
        fakeRequest.setId(1L);
        EmployeeResponse fakeEmployee = new EmployeeResponse();
        fakeEmployee.setEmployeeId("EMP-0001");
        fakeEmployee.setDepartment("IT");
        fakeEmployee.setUserPrincipalName("jdoe@mamarcoadamegmail.onmicrosoft.com");

        GraphUser fakeCreatedUser = new GraphUser();
        fakeCreatedUser.setId("fake-entra-object-id-123");

        ProvisioningPlan fakePlan = new ProvisioningPlan(groups, licenses);

        when(provisioningRequestService.createProvisioningRequest("EMP-0001")).thenReturn(fakeRequest);
        when(employeeService.findByEmployeeId("EMP-0001")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);
        when(graphService.createUser("EMP-0001")).thenReturn(fakeCreatedUser);

        lenient().doThrow(new OptimisticLockingFailureException("Locked out"))
                .when(provisioningRequestService)
                .transitionTo(1L, ProvisioningRequestStatus.COMPLETED, "fake-entra-object-id-123");

        orchestratorServiceImp.orchestrate("EMP-0001");

        verify(provisioningRequestService, never()).transitionTo(1L, ProvisioningRequestStatus.FAILED);
        verify(employeeService).addEmloyeeEntraObjectId("EMP-0001", "fake-entra-object-id-123");
    }

    @Test
    void orchestrateFails_whenGraphRejectsCreateUser() {
        ProvisioningRequest fakeRequest = new ProvisioningRequest();
        fakeRequest.setId(1L);
        EmployeeResponse fakeEmployee = new EmployeeResponse();
        fakeEmployee.setEmployeeId("EMP-0001");
        fakeEmployee.setDepartment("IT");

        ProvisioningPlan fakePlan = new ProvisioningPlan(List.of("group1"), List.of("license1"));

        when(provisioningRequestService.createProvisioningRequest("EMP-0001")).thenReturn(fakeRequest);
        when(employeeService.findByEmployeeId("EMP-0001")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);

        when(graphService.createUser("EMP-0001"))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        orchestratorServiceImp.orchestrate("EMP-0001");

        verify(provisioningRequestService).transitionTo(1L, ProvisioningRequestStatus.FAILED);
        verify(employeeService, never()).addEmloyeeEntraObjectId(any(), any());
        verify(provisioningRequestService, never()).transitionTo(eq(1L), eq(ProvisioningRequestStatus.COMPLETED), any());
    }

}