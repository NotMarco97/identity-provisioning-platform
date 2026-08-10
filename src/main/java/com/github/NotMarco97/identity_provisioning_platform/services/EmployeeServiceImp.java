package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.CreateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.dto.UpdateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.entities.Employee;
import com.github.NotMarco97.identity_provisioning_platform.entities.EmployeeStatus;
import com.github.NotMarco97.identity_provisioning_platform.repositories.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    public EmployeeServiceImp(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        Employee newEmployee = new Employee();
        EmployeeResponse newEmployeeResponse = new EmployeeResponse();

        newEmployee.setFirstName(createEmployeeRequest.getFirstName());
        newEmployee.setLastName(createEmployeeRequest.getLastName());
        newEmployee.setDepartment(createEmployeeRequest.getDepartment());
        newEmployee.setJobTitle(createEmployeeRequest.getJobTitle());
        newEmployee.setSalary(createEmployeeRequest.getSalary());
        newEmployee.setStatus(EmployeeStatus.ACTIVE);

        String emailNameSection = newEmployee.getFirstName().toLowerCase().charAt(0) + newEmployee.getLastName().toLowerCase();
        String email = emailNameSection + "@company.com";

        int counter = 0;
        while(employeeRepository.existsByEmail(email)){
            counter++;
            email = emailNameSection + counter +  "@company.com";
        }
        newEmployee.setEmail(email);

        employeeRepository.save(newEmployee);
        newEmployee.setEmployeeId("EMP-" + String.format("%04d", newEmployee.getId()));
        employeeRepository.save(newEmployee);

        newEmployeeResponse.setFirstName(newEmployee.getFirstName());
        newEmployeeResponse.setLastName(newEmployee.getLastName());
        newEmployeeResponse.setDepartment(newEmployee.getDepartment());
        newEmployeeResponse.setJobTitle(newEmployee.getJobTitle());
        newEmployeeResponse.setSalary(newEmployee.getSalary());
        newEmployeeResponse.setEmployeeId(newEmployee.getEmployeeId());
        newEmployeeResponse.setEmail(newEmployee.getEmail());
        newEmployeeResponse.setCreatedAt(newEmployee.getCreatedAt().toString());
        newEmployeeResponse.setUpdatedAt(newEmployee.getUpdatedAt().toString());
        newEmployeeResponse.setStatus(newEmployee.getStatus().name());

        return newEmployeeResponse;
    }

    @Override
    public EmployeeResponse findByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow();
        EmployeeResponse employeeResponse = new EmployeeResponse();

        employeeResponse.setFirstName(employee.getFirstName());
        employeeResponse.setLastName(employee.getLastName());
        employeeResponse.setDepartment(employee.getDepartment());
        employeeResponse.setJobTitle(employee.getJobTitle());
        employeeResponse.setSalary(employee.getSalary());
        employeeResponse.setEmployeeId(employee.getEmployeeId());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setCreatedAt(employee.getCreatedAt().toString());
        employeeResponse.setUpdatedAt(employee.getUpdatedAt().toString());
        employeeResponse.setStatus(employee.getStatus().name());

        return employeeResponse;
    }

    @Override
    public void deleteByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow();
        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(String employeeId, UpdateEmployeeRequest updateEmployeeRequest) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElseThrow();
        EmployeeResponse employeeResponse = new EmployeeResponse();

        if (updateEmployeeRequest.getDepartment() != null) {
            employee.setDepartment(updateEmployeeRequest.getDepartment());
        }
        if (updateEmployeeRequest.getJobTitle() != null) {
            employee.setJobTitle(updateEmployeeRequest.getJobTitle());
        }
        if (updateEmployeeRequest.getSalary() != null) {
            employee.setSalary(updateEmployeeRequest.getSalary());
        }

        employeeRepository.save(employee);

        employeeResponse.setFirstName(employee.getFirstName());
        employeeResponse.setLastName(employee.getLastName());
        employeeResponse.setDepartment(employee.getDepartment());
        employeeResponse.setJobTitle(employee.getJobTitle());
        employeeResponse.setSalary(employee.getSalary());
        employeeResponse.setEmployeeId(employee.getEmployeeId());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setCreatedAt(employee.getCreatedAt().toString());
        employeeResponse.setUpdatedAt(employee.getUpdatedAt().toString());
        employeeResponse.setStatus(employee.getStatus().name());

        return employeeResponse;
    }

    @Override
    public List<EmployeeResponse> findAllEmployees() {
       List<Employee> employeeList = employeeRepository.findAll();
       List<EmployeeResponse> employeeResponseList = new ArrayList<>();

       // Go over each employee in the list and assign it to the response
       for(int i = 0; i < employeeList.size(); i++){
           Employee employee = employeeList.get(i);
           EmployeeResponse employeeResponse = new EmployeeResponse();

           employeeResponse.setFirstName(employee.getFirstName());
           employeeResponse.setLastName(employee.getLastName());
           employeeResponse.setDepartment(employee.getDepartment());
           employeeResponse.setJobTitle(employee.getJobTitle());
           employeeResponse.setSalary(employee.getSalary());
           employeeResponse.setEmployeeId(employee.getEmployeeId());
           employeeResponse.setEmail(employee.getEmail());
           employeeResponse.setCreatedAt(employee.getCreatedAt().toString());
           employeeResponse.setUpdatedAt(employee.getUpdatedAt().toString());
           employeeResponse.setStatus(employee.getStatus().name());

           employeeResponseList.add(employeeResponse);

       }
        return employeeResponseList;
    }
}
