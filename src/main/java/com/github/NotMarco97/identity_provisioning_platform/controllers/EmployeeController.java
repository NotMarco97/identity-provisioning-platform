package com.github.NotMarco97.identity_provisioning_platform.controllers;

import com.github.NotMarco97.identity_provisioning_platform.dto.CreateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.dto.UpdateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.services.EmployeeService;
import com.github.NotMarco97.identity_provisioning_platform.services.EmployeeServiceImp;
import com.github.NotMarco97.identity_provisioning_platform.services.IdempotencyRecordService;
import com.github.NotMarco97.identity_provisioning_platform.services.OrchestratorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final IdempotencyRecordService idempotencyRecordService;
    private final ObjectMapper objectMapper;
    private final OrchestratorService orchestratorService;


    public EmployeeController(EmployeeService employeeService,  IdempotencyRecordService idempotencyRecordService, ObjectMapper objectMapper,
                              OrchestratorService orchestratorService) {
        this.employeeService = employeeService;
        this.idempotencyRecordService = idempotencyRecordService;
        this.objectMapper = objectMapper;
        this.orchestratorService = orchestratorService;
    }

    @PostMapping()
    public EmployeeResponse createEmployee(@RequestHeader("Idempotency-Key") String key, @Valid @RequestBody CreateEmployeeRequest createEmployeeRequest){
        Optional<String> exists = idempotencyRecordService.checkForExistingKey(key);
        if(exists.isPresent()){
            return objectMapper.readValue(exists.get(), EmployeeResponse.class);
        }

        idempotencyRecordService.markInProgress(key);
        EmployeeResponse employeeResponse = employeeService.createEmployee(createEmployeeRequest);
        orchestratorService.orchestrate(employeeResponse.getEmployeeId());
        idempotencyRecordService.markCompleted(key, objectMapper.writeValueAsString(employeeResponse));

        return employeeResponse;
    }

    @GetMapping()
    public List<EmployeeResponse> getAllEmployees(){
        return employeeService.findAllEmployees();
    }

    @GetMapping("{employeeId}")
    public EmployeeResponse getEmployee(@PathVariable String employeeId){

        return employeeService.findByEmployeeId(employeeId);
    }

    @DeleteMapping("{employeeId}")
    public void deleteEmployee(@PathVariable String employeeId){
        employeeService.deleteByEmployeeId(employeeId);
    }

    @PatchMapping("{employeeId}")
    public EmployeeResponse updateEmployee(@PathVariable String employeeId, @RequestBody UpdateEmployeeRequest updateEmployeeRequest){
        return employeeService.updateEmployee(employeeId, updateEmployeeRequest);
    }

}
