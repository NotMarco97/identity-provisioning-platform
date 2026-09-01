package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GraphUserRequestServiceImpTest {
    @Mock
    EmployeeService employeeService;


    @InjectMocks
    private GraphUserRequestServiceImp graphUserRequestServiceImp;

    @Test
    void buildRequestProvidesTheCorrectMailNickname() {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmail("jdoe@company.com");

        assertEquals("jdoe", employeeResponse.getEmail().substring(0, employeeResponse.getEmail().indexOf("@")));
    }

    @Test
    void buildRequestProvidesTheCorrectDisplayName(){
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setFirstName("John");
        employeeResponse.setLastName("Doe");

        assertEquals("John Doe", employeeResponse.getFirstName() + " "  + employeeResponse.getLastName());

    }


}