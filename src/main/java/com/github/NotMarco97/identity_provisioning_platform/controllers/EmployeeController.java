package com.github.NotMarco97.identity_provisioning_platform.controllers;

import com.github.NotMarco97.identity_provisioning_platform.dto.CreateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.dto.UpdateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.services.EmployeeService;
import com.github.NotMarco97.identity_provisioning_platform.services.EmployeeServiceImp;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeServiceImp employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping()
    public EmployeeResponse createEmployee(@Valid @RequestBody CreateEmployeeRequest createEmployeeRequest){
        return employeeService.createEmployee(createEmployeeRequest);
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
