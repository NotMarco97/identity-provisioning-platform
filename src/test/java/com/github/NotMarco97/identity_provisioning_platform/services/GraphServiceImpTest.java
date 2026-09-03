package com.github.NotMarco97.identity_provisioning_platform.services;

import com.github.NotMarco97.identity_provisioning_platform.Oauth.GraphTokenService;
import com.github.NotMarco97.identity_provisioning_platform.entities.GraphUser;
import com.github.NotMarco97.identity_provisioning_platform.graph.GraphCreateUserRequest;
import com.github.NotMarco97.identity_provisioning_platform.graph.PasswordProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import org.springframework.http.HttpMethod;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(MockitoExtension.class)
class GraphServiceImpTest {
    private MockRestServiceServer mockServer;
    private RestClient restClient;
    private GraphServiceImp graphServiceImp;
    @Mock
    GraphTokenService graphTokenService;
    @Mock
    GraphUserRequestService graphUserRequestService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        restClient = builder.build();
        graphServiceImp = new GraphServiceImp(graphTokenService, graphUserRequestService, restClient);
    }

    @Test
    void userPrincipalNameExists_returnsTrue_whenUserFound() {
        when(graphTokenService.getAccessToken()).thenReturn("fake-token");

        String testUpn = "test.user@example.com";

        mockServer.expect(requestTo(org.hamcrest.Matchers.containsString(testUpn)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                {"value":[{"id":"123","userPrincipalName":"%s"}]}
                """.formatted(testUpn), MediaType.APPLICATION_JSON));

        boolean result = graphServiceImp.userPrincipalNameExists(testUpn);

        assertTrue(result);
    }

    @Test
    void userPrincipalNameExists_returnsFalse_WhenNotFound() {
        when(graphTokenService.getAccessToken()).thenReturn("fake-token");

        String testUpn = "test.user@example.com";

        mockServer.expect(requestTo(org.hamcrest.Matchers.containsString(testUpn)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                {"value":[]}
                """, MediaType.APPLICATION_JSON));

        boolean result = graphServiceImp.userPrincipalNameExists(testUpn);

        assertFalse(result);
    }

    @Test
    void userPrincipalNameExists_throws_whenGraphReturnsError() {
        when(graphTokenService.getAccessToken()).thenReturn("fake-token");
        String testUpn = "test.user@example.com";

        mockServer.expect(requestTo(org.hamcrest.Matchers.containsString(testUpn)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThrows(HttpClientErrorException.class, () -> graphServiceImp.userPrincipalNameExists(testUpn));
    }

    @Test
    void createUser() {
        GraphCreateUserRequest request = new GraphCreateUserRequest();
        request.setAccountEnabled(true);
        request.setUserPrincipalName("test.user@example.com");
        request.setMailNickname("test-user");
        request.setDisplayName("test-user");
        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.setPassword("password");
        passwordProfile.setForceChangePasswordNextSignIn(true);
        request.setPasswordProfile(passwordProfile);

        when(graphTokenService.getAccessToken()).thenReturn("fake-token");
        when(graphUserRequestService.buildRequest("EMP-0001")).thenReturn(request);

        mockServer.expect(requestTo("https://graph.microsoft.com/v1.0/users"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userPrincipalName").value("test.user@example.com"))
                .andExpect(jsonPath("$.mailNickname").value("test-user"))
                .andRespond(withSuccess("""
            {"id":"generated-id-123","userPrincipalName":"test.user@example.com"}
            """, MediaType.APPLICATION_JSON));



        GraphUser result = graphServiceImp.createUser("EMP-0001");

        assertEquals("generated-id-123", result.getId());
        assertEquals("test.user@example.com", result.getUserPrincipalName());

    }

    @Test
    void createUser_throws_whenGraphRejectsRequest() {
        GraphCreateUserRequest request = new GraphCreateUserRequest();
        request.setAccountEnabled(true);
        request.setUserPrincipalName("");
        request.setMailNickname("test-user");
        request.setDisplayName("test-user");
        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.setPassword("password");
        passwordProfile.setForceChangePasswordNextSignIn(true);
        request.setPasswordProfile(passwordProfile);

        when(graphTokenService.getAccessToken()).thenReturn("fake-token");
        when(graphUserRequestService.buildRequest("EMP-0001")).thenReturn(request);

        mockServer.expect(requestTo("https://graph.microsoft.com/v1.0/users"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                        {"error":{"code":"Request_BadRequest","message":"Property userPrincipalName value is required but is empty or missing."}}
                        """));

        assertThrows(HttpClientErrorException.class, () -> graphServiceImp.createUser("EMP-0001"));
    }
}