package com.swachhsathi.dto;

import com.swachhsathi.entity.JobApplicationStatus;
import jakarta.validation.constraints.NotNull;

public class JobApplicationReviewDTO {

    @NotNull(message = "Status is required (APPROVED or REJECTED)")
    private JobApplicationStatus status;

    private String adminNotes;

    public JobApplicationReviewDTO() {
    }

    public JobApplicationReviewDTO(JobApplicationStatus status, String adminNotes) {
        this.status = status;
        this.adminNotes = adminNotes;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
