package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.CreateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.dto.UpdateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.Employee;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest createEmployeeRequest);
    EmployeeResponse findByEmployeeId(String employeeId);
    void deleteByEmployeeId(String employeeId);
    EmployeeResponse updateEmployee(String employeeId, UpdateEmployeeRequest updateEmployeeRequest);
    List<EmployeeResponse> findAllEmployees();
    void addEmloyeeEntraObjectId(String employeeId, String entraObjectId);
}
