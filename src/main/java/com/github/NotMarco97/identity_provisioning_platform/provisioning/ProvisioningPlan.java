package com.github.NotMarco97.identity_provisioning_platform.provisioning;

import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public class ProvisioningPlan {
    private final List<String> groups;
    private final List<String> licences;
    public ProvisioningPlan(List<String> groups, List<String> licences) {
        this.groups = groups;
        this.licences = licences;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<String> getLicences() {
        return licences;
    }
}
