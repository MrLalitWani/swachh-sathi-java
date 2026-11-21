package com.swachhsathi.dto;

import com.swachhsathi.entity.JobApplicationStatus;
import com.swachhsathi.entity.WorkerJobApplication;

import java.time.LocalDateTime;

public class JobApplicationResponseDTO {

    private Long id;
    private String applicantName;
    private String email;
    private String phone;
    private String address;
    private Integer experienceYears;
    private String idProofUrl;
    private JobApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime reviewedAt;
    private String adminNotes;

    public JobApplicationResponseDTO() {
    }

    public static JobApplicationResponseDTO fromEntity(WorkerJobApplication app) {
        if (app == null) return null;
        JobApplicationResponseDTO dto = new JobApplicationResponseDTO();
        dto.setId(app.getId());
        dto.setApplicantName(app.getApplicantName());
        dto.setEmail(app.getEmail());
        dto.setPhone(app.getPhone());
        dto.setAddress(app.getAddress());
        dto.setExperienceYears(app.getExperienceYears());
        dto.setIdProofUrl(app.getIdProofUrl());
        dto.setStatus(app.getStatus());
        dto.setAppliedAt(app.getAppliedAt());
        dto.setReviewedAt(app.getReviewedAt());
        dto.setAdminNotes(app.getAdminNotes());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getIdProofUrl() {
        return idProofUrl;
    }

    public void setIdProofUrl(String idProofUrl) {
        this.idProofUrl = idProofUrl;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
