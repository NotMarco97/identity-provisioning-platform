package com.github.NotMarco97.identity_provisioning_platform.provisioning;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessTemplate {
    private final List<String> groups;
    private final List<String> licences;

    public AccessTemplate(List<String> groups, List<String> licences) {
        this.groups = groups;
        this.licences = licences;
    }

    public List<String> getGroups() {
        return groups;
    }


    public List<String> getLicenses() {
        return licences;
    }
}
