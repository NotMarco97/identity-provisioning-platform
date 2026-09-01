package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.dto.EmployeeResponse;
import com.github.NotMarco97.identity_provisioning_platform.graph.GraphCreateUserRequest;
import com.github.NotMarco97.identity_provisioning_platform.graph.PasswordProfile;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class GraphUserRequestServiceImp implements GraphUserRequestService {
    private final EmployeeService employeeService;
    public GraphUserRequestServiceImp(EmployeeService employeeServiceImpl) {
        this.employeeService = employeeServiceImpl;
    }

    @Override
    public GraphCreateUserRequest buildRequest(String employeeId) {
        EmployeeResponse employeeResponse = employeeService.findByEmployeeId(employeeId);
        String mailNickname = employeeResponse.getEmail().substring(0, employeeResponse.getEmail().indexOf("@"));
        GraphCreateUserRequest graphCreateUserRequest = new GraphCreateUserRequest();
        graphCreateUserRequest.setAccountEnabled(true);
        graphCreateUserRequest.setDisplayName(employeeResponse.getFirstName() + " " + employeeResponse.getLastName());
        graphCreateUserRequest.setMailNickname(mailNickname);
        graphCreateUserRequest.setUserPrincipalName(employeeResponse.getUserPrincipalName());

        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.setForceChangePasswordNextSignIn(true);

        String password = RandomStringUtils.secure().nextAlphabetic(6)
                + RandomStringUtils.secure().nextNumeric(2)
                + "!";
        passwordProfile.setPassword(password);
        graphCreateUserRequest.setPasswordProfile(passwordProfile);

        return graphCreateUserRequest;
    }
}
