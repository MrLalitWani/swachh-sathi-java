package com.swachhsathi.dto;

import jakarta.validation.constraints.NotNull;

public class VerifyComplaintDTO {

    @NotNull(message = "Approval status is required")
    private Boolean approved;

    private String feedback;

    public VerifyComplaintDTO() {
    }

    public VerifyComplaintDTO(Boolean approved, String feedback) {
        this.approved = approved;
        this.feedback = feedback;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
