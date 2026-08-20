package com.github.NotMarco97.identity_provisioning_platform.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class AuditEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String actor;
    private String statusChange;
    @CreationTimestamp
    private LocalDateTime timeStamp;
    private String targetEmployee;
    private String outcome;
    private Long requestId;

    public String getActor() {
        return actor;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }
    public String getStatusChange() {
        return statusChange;
    }
    public void setStatusChange(String statusChange) {
        this.statusChange = statusChange;
    }
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getTargetEmployee() {
        return targetEmployee;
    }
    public void setTargetEmployee(String targetEmployee) {
        this.targetEmployee = targetEmployee;
    }
    public String getOutcome() {
        return outcome;
    }
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
    public Long getRequestId() {
        return requestId;
    }
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    public Long getId() {
        return id;
    }
}
