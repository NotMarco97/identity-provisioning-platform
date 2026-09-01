package com.github.NotMarco97.identity_provisioning_platform.graph;

import com.github.NotMarco97.identity_provisioning_platform.entities.GraphUser;

import java.util.List;

public class GraphUserListResponse {
    private List<GraphUser> value;
    public List<GraphUser> getValue() {
        return value;
    }
    public void setValue(List<GraphUser> value) {
        this.value = value;
    }
}
