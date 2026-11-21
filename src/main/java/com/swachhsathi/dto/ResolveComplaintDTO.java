package com.swachhsathi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResolveComplaintDTO {

    private String proofPhotoUrl;

    @NotBlank(message = "Resolution notes are required")
    @Size(min = 5, max = 1000, message = "Resolution notes must be between 5 and 1000 characters")
    private String resolutionNotes;

    public ResolveComplaintDTO() {
    }

    public ResolveComplaintDTO(String proofPhotoUrl, String resolutionNotes) {
        this.proofPhotoUrl = proofPhotoUrl;
        this.resolutionNotes = resolutionNotes;
    }

    public String getProofPhotoUrl() {
        return proofPhotoUrl;
    }

    public void setProofPhotoUrl(String proofPhotoUrl) {
        this.proofPhotoUrl = proofPhotoUrl;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }
}
