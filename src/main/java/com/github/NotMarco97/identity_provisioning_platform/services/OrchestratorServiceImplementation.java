package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.entities.Employee;
import com.github.NotMarco97.identity_provisioning_platform.entities.GraphUser;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlan;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlanResolver;
import com.github.NotMarco97.identity_provisioning_platform.repositories.EmployeeRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class OrchestratorServiceImplementation implements OrchestratorService {
    private final EmployeeService employeeServiceImp;
    private final ProvisioningPlanResolver provisioningPlanResolver;
    private final ProvisioningRequestService provisioningRequestServiceImp;
    private final GraphServiceImp graphServiceImp;
    private final EmployeeRepository employeeRepository;

    public OrchestratorServiceImplementation(EmployeeService employeeServiceImp, ProvisioningRequestService provisioningRequestServiceImp,
                                             ProvisioningPlanResolver provisioningPlanResolver, GraphServiceImp graphServiceImp, EmployeeRepository employeeRepositoryImp) {
        this.employeeServiceImp = employeeServiceImp;
        this.provisioningPlanResolver = provisioningPlanResolver;
        this.provisioningRequestServiceImp = provisioningRequestServiceImp;
        this.graphServiceImp = graphServiceImp;
        this.employeeRepository = employeeRepositoryImp;
    }

    @Override
    public void orchestrate(String employeeId) {
        ProvisioningRequest provisioningRequest = provisioningRequestServiceImp.createProvisioningRequest(employeeId);
        provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.VALIDATED);

        EmployeeResponse employeeResponse = employeeServiceImp.findByEmployeeId(employeeId);
        ProvisioningPlan provisioningPlan = provisioningPlanResolver.resolvePlan(employeeResponse.getDepartment());
        provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.PLANNED);

        provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.PENDING);

        try {
            GraphUser createdUser = graphServiceImp.createUser(employeeId);
            String entraObjectId = createdUser.getId();
            employeeServiceImp.addEmloyeeEntraObjectId(employeeId, entraObjectId);

            provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.COMPLETED, entraObjectId);
        } catch (OptimisticLockingFailureException e) {
            return;
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.out.println("Graph error response: " + e.getResponseBodyAsString());
            provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.FAILED);
        } catch (RuntimeException e) {
            provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.FAILED);
        }

    }
}
