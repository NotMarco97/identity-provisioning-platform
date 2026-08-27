package com.github.NotMarco97.identity_provisioning_platform.provisioning;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProvisioningPlanResolver {
    private final Map<String, AccessTemplate> templates = new HashMap<>();

    public ProvisioningPlanResolver() {
        templates.put("IT", new AccessTemplate(List.of("IT-Team"), List.of("M365-Standard")));
        templates.put("Sales", new AccessTemplate(List.of("Sales-Team"), List.of("M365-Standard")));
    }

    public ProvisioningPlan resolvePlan(String department) {
        AccessTemplate template = templates.get(department);
        if (template == null) {
            throw new NoSuchElementException("No access template found for department: " + department);
        }
        return new ProvisioningPlan(template.getGroups(), template.getLicenses());
    }
}
