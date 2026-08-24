package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.entities.Employee;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.ProvisioningRequestStatus;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlan;
import com.github.NotMarco97.identity_provisioning_platform.provisioning.ProvisioningPlanResolver;
import org.springframework.stereotype.Service;

@Service
public class OrchestratorServiceImplementation implements OrchestratorService {
    private final EmployeeServiceImp employeeServiceImp;
    private final ProvisioningPlanResolver provisioningPlanResolver;
    private final ProvisioningRequestServiceImp provisioningRequestServiceImp;
    private final GraphProviderService graphProviderService;

    public OrchestratorServiceImplementation(EmployeeServiceImp employeeServiceImp, ProvisioningRequestServiceImp provisioningRequestServiceImp,
                                             ProvisioningPlanResolver provisioningPlanResolver,  GraphProviderService graphProviderService) {
        this.employeeServiceImp = employeeServiceImp;
        this.provisioningPlanResolver = provisioningPlanResolver;
        this.provisioningRequestServiceImp = provisioningRequestServiceImp;
        this.graphProviderService = graphProviderService;
    }

    @Override
    public void orchestrate(String employeeId) {
        ProvisioningRequest provisioningRequest = provisioningRequestServiceImp.createProvisioningRequest(employeeId);
        provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.VALIDATED);

        EmployeeResponse employeeResponse = employeeServiceImp.findByEmployeeId(employeeId);
        ProvisioningPlan provisioningPlan = provisioningPlanResolver.resolvePlan(employeeResponse.getDepartment());
        provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.PLANNED);

        provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.PENDING);

        try{
            graphProviderService.createUser(employeeId, provisioningPlan);
            provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.COMPLETED);
        }catch (RuntimeException e){
            provisioningRequestServiceImp.transitionTo(provisioningRequest.getId(), ProvisioningRequestStatus.FAILED);
        }

    }
}
