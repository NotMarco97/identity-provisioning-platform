package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.entities.GraphUser;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlan;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlanResolver;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class OrchestratorServiceImp implements OrchestratorService {
    private final EmployeeService employeeServiceImp;
    private final ProvisioningPlanResolver provisioningPlanResolver;
    private final ProvisioningRequestService provisioningRequestServiceImp;
    private final GraphService graphService;

    public OrchestratorServiceImp(EmployeeService employeeServiceImp, ProvisioningRequestService provisioningRequestServiceImp,
                                  ProvisioningPlanResolver provisioningPlanResolver, GraphService graphService) {
        this.employeeServiceImp = employeeServiceImp;
        this.provisioningPlanResolver = provisioningPlanResolver;
        this.provisioningRequestServiceImp = provisioningRequestServiceImp;
        this.graphService = graphService;
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
            GraphUser createdUser = graphService.createUser(employeeId);
            String entraObjectId = createdUser.getId();
            employeeServiceImp.addEmployeeEntraObjectId(employeeId, entraObjectId);

            provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.COMPLETED, entraObjectId);
        } catch (OptimisticLockingFailureException e) {
            return;

        } catch (RuntimeException e) {

            provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.FAILED);
        }

    }
}
