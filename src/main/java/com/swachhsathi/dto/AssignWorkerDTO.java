package com.swachhsathi.dto;

import jakarta.validation.constraints.NotNull;

public class AssignWorkerDTO {

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    public AssignWorkerDTO() {
    }

    public AssignWorkerDTO(Long workerId) {
        this.workerId = workerId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }
}
