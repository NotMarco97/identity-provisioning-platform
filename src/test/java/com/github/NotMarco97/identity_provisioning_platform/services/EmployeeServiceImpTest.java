package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.CreateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.entities.Employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.github.NotMarco97.identity_provisioning_platform.entities.EmployeeStatus;
import com.github.NotMarco97.identity_provisioning_platform.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImpTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImp employeeServiceImp;

    @Test
    void createEmployeeShouldGenerateProperEmployeeId() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDepartment("IT");
        request.setJobTitle("Help Desk");
        request.setSalary(40000);

        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);

        doAnswer(invocation-> {Employee employeeARG = invocation.getArgument(0);
        employeeARG.setId(42L);
        employeeARG.setUpdatedAt(LocalDateTime.now());
        employeeARG.setCreatedAt(LocalDateTime.now());
        return employeeARG;}).when(employeeRepository).save(any(Employee.class));

        EmployeeResponse response = employeeServiceImp.createEmployee(request);

        assertEquals("EMP-0042", response.getEmployeeId());

    }

    @Test
    void createEmployeeShouldAppendSuffix_WhenEmailAlreadyExists() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDepartment("IT");
        request.setJobTitle("Help Desk");
        request.setSalary(40000);

        when(employeeRepository.existsByEmail("jdoe@company.com")).thenReturn(true);
        when(employeeRepository.existsByEmail("jdoe1@company.com")).thenReturn(false);

        doAnswer(invocation-> {Employee employeeARG = invocation.getArgument(0);
            employeeARG.setId(1L);
            employeeARG.setUpdatedAt(LocalDateTime.now());
            employeeARG.setCreatedAt(LocalDateTime.now());
            return employeeARG;}).when(employeeRepository).save(any(Employee.class));

        EmployeeResponse response = employeeServiceImp.createEmployee(request);

        assertEquals("jdoe1@company.com", response.getEmail());

    }

    @Test
    void createEmployeeShouldAppendSuffix_WhenUPN_AlreadyExists() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDepartment("IT");
        request.setJobTitle("Help Desk");
        request.setSalary(40000);

        when(employeeRepository.existsByUserPrincipalName("jdoe@mamarcoadamegmail.onmicrosoft.com")).thenReturn(true);
        when(employeeRepository.existsByUserPrincipalName("jdoe1@mamarcoadamegmail.onmicrosoft.com")).thenReturn(false);

        doAnswer(invocation-> {Employee employeeARG = invocation.getArgument(0);
            employeeARG.setId(1L);
            employeeARG.setUpdatedAt(LocalDateTime.now());
            employeeARG.setCreatedAt(LocalDateTime.now());
            return employeeARG;}).when(employeeRepository).save(any(Employee.class));

        EmployeeResponse response = employeeServiceImp.createEmployee(request);

        assertEquals("jdoe1@mamarcoadamegmail.onmicrosoft.com", response.getUserPrincipalName());

    }

    @Test
    void findEmployeeByIdShouldReturnCorrectEmployee() {
        String employeeId = "EMP-0042";
        Employee fakeEmployee = new Employee();
        fakeEmployee.setEmployeeId(employeeId);
        fakeEmployee.setFirstName("John");
        fakeEmployee.setLastName("Doe");
        fakeEmployee.setDepartment("IT");
        fakeEmployee.setJobTitle("Help Desk");
        fakeEmployee.setSalary(40000.0);
        fakeEmployee.setStatus(EmployeeStatus.ACTIVE);
        fakeEmployee.setEmail("jdoe@company.com");
        fakeEmployee.setUserPrincipalName("jdoe@mamarcoadamegmail.onmicrosoft.com");
        fakeEmployee.setCreatedAt(LocalDateTime.now());
        fakeEmployee.setUpdatedAt(LocalDateTime.now());

        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(Optional.of(fakeEmployee));

        EmployeeResponse response = employeeServiceImp.findByEmployeeId(employeeId);

        assertEquals(employeeId, response.getEmployeeId());
    }

    @Test
    void findEmployeeByIdShouldThrow_whenEmployeeNotFound() {
        String employeeId = "EMP-9999";

        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> employeeServiceImp.findByEmployeeId(employeeId));
    }
}