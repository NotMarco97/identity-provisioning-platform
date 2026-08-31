package com.github.NotMarco97.identity_provisioning_platform.graph;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GraphClientConfig {
    @Bean
    public RestClient graphRestClient() {
        return RestClient.create();
    }
}