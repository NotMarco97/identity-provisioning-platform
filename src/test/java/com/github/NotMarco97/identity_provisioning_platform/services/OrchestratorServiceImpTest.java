package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlan;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlanResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrchestratorServiceImpTest {
    @Mock
    private ProvisioningRequestService provisioningRequestServiceImp;
    @Mock
    private EmployeeService employeeServiceImp;
    @Mock
    private ProvisioningPlanResolver provisioningPlanResolver;
    @Mock
    private GraphServiceImp graphServiceImp;

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

        ProvisioningPlan fakePlan = new ProvisioningPlan(groups, licenses);

        when(provisioningRequestServiceImp.createProvisioningRequest("EMP-0001")).thenReturn(fakeRequest);
        when(employeeServiceImp.findByEmployeeId("EMP-0001")).thenReturn(fakeEmployee);
        when(provisioningPlanResolver.resolvePlan(any())).thenReturn(fakePlan);
        doNothing().when(graphServiceImp).createUser(any());

        orchestratorServiceImp.orchestrate("EMP-0001");

        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.VALIDATED);
        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.PLANNED);
        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.PENDING);
        verify(provisioningRequestServiceImp).transitionTo(1L, ProvisioningRequestStatus.COMPLETED);
    }

}