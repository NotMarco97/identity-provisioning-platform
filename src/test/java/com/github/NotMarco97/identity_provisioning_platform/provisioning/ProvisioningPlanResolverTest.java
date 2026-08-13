package com.github.NotMarco97.identity_provisioning_platform.provisioning;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ProvisioningPlanResolverTest {

    @Test
    void resolvePlanShouldReturnCorrectTemplate() {
        ProvisioningPlanResolver resolver = new ProvisioningPlanResolver();

        ProvisioningPlan plan = resolver.resolvePlan("IT");

        assertEquals(List.of("IT-Team"), plan.getGroups());
        assertEquals(List.of("M365-Standard"), plan.getLicences());
    }

    @Test
    void resolvePlanShouldThrowForUnknownDepartment() {
        ProvisioningPlanResolver resolver = new ProvisioningPlanResolver();

        assertThrows(NoSuchElementException.class, () -> resolver.resolvePlan("Marketing"));
    }
}