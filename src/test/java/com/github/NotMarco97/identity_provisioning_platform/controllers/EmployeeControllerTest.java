package com.github.NotMarco97.identity_provisioning_platform.controllers;

import com.github.NotMarco97.identity_provisioning_platform.dto.CreateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.dto.UpdateEmployeeRequest;
import com.github.NotMarco97.identity_provisioning_platform.services.EmployeeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void createEmployeeReturnsEmployee() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setJobTitle("Help Desk");
        request.setSalary(45000);
        request.setDepartment("IT");

        EmployeeResponse employeeResponse = new  EmployeeResponse();
        employeeResponse.setEmployeeId("EMP-0001");
        employeeResponse.setFirstName("John");
        employeeResponse.setLastName("Doe");
        employeeResponse.setJobTitle("Help Desk");
        employeeResponse.setSalary(45000);
        employeeResponse.setDepartment("IT");
        employeeResponse.setStatus("ACTIVE");
        employeeResponse.setCreatedAt(LocalDateTime.now().toString());
        employeeResponse.setUpdatedAt(LocalDateTime.now().toString());
        employeeResponse.setEmail("jdoe@company.com");

        when(employeeService.createEmployee(any(CreateEmployeeRequest.class))).thenReturn(employeeResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP-0001"));
    }

    @Test
    void createEmployeeReturns404WhenInvalid() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("John");
        request.setLastName("");
        request.setJobTitle("Help Desk");
        request.setSalary(45000);
        request.setDepartment("IT");

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployeeId("EMP-0001");
        employeeResponse.setFirstName("John");
        employeeResponse.setLastName("Doe");
        employeeResponse.setJobTitle("Help Desk");
        employeeResponse.setSalary(45000);
        employeeResponse.setDepartment("IT");
        employeeResponse.setStatus("ACTIVE");
        employeeResponse.setCreatedAt(LocalDateTime.now().toString());
        employeeResponse.setUpdatedAt(LocalDateTime.now().toString());
        employeeResponse.setEmail("jdoe@company.com");

        when(employeeService.createEmployee(any(CreateEmployeeRequest.class))).thenReturn(employeeResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createEmployeeReturns409WhenAlreadyExists() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setJobTitle("Help Desk");
        request.setSalary(45000);
        request.setDepartment("IT");

        when(employeeService.createEmployee(any(CreateEmployeeRequest.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate Entry"));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void getEmployeeReturnsEmployee() throws Exception {
        when(employeeService.findByEmployeeId("EMP-0001")).thenReturn(new EmployeeResponse());

        mockMvc.perform(get("/employees/EMP-0001"))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeeReturns404WhenInvalid() throws Exception {
        when(employeeService.findByEmployeeId("EMP-0001")).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/employees/EMP-0001"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllEmployeesReturnsEmployees() throws Exception {
        when(employeeService.findAllEmployees()).thenReturn(List.of(new EmployeeResponse()));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEmployeesReturns404WhenInvalid() throws Exception {
        when(employeeService.findAllEmployees()).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/employees"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployeeByIdDeletesEmployee() throws Exception {
        doNothing().when(employeeService).deleteByEmployeeId("EMP-0001");

        mockMvc.perform(delete("/employees/EMP-0001"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteEmployeeByIdReturns404WhenInvalid() throws Exception {
        doThrow(new NoSuchElementException()).when(employeeService).deleteByEmployeeId("EMP-0001");

        mockMvc.perform(delete("/employees/EMP-0001"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployeeByIdUpdatesEmployee() throws Exception {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setDepartment("IT");
        request.setJobTitle("Help Desk");

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployeeId("EMP-0001");
        employeeResponse.setFirstName("John");
        employeeResponse.setLastName("Doe");
        employeeResponse.setJobTitle("Help Desk");
        employeeResponse.setSalary(45000);
        employeeResponse.setDepartment("IT");
        employeeResponse.setStatus("ACTIVE");
        employeeResponse.setCreatedAt(LocalDateTime.now().toString());
        employeeResponse.setUpdatedAt(LocalDateTime.now().toString());
        employeeResponse.setEmail("jdoe@company.com");

        when(employeeService.updateEmployee(anyString(), any(UpdateEmployeeRequest.class))).thenReturn(employeeResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/employees/EMP-0001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobTitle").value("Help Desk"));
    }

    @Test
    void updateEmployeeByIdReturns404WhenInvalid() throws Exception {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setDepartment("IT");
        request.setJobTitle("Help Desk");

        when(employeeService.updateEmployee(anyString(), any(UpdateEmployeeRequest.class))).thenThrow(new NoSuchElementException());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/employees/EMP-0001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }
}