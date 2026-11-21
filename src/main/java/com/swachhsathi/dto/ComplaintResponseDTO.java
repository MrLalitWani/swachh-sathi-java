package com.swachhsathi.dto;

import com.swachhsathi.entity.Complaint;
import com.swachhsathi.entity.ComplaintStatus;
import com.swachhsathi.entity.PriorityLevel;
import com.swachhsathi.entity.WasteType;

import java.time.LocalDateTime;

public class ComplaintResponseDTO {

    private Long id;
    private UserDTO citizen;
    private WasteType wasteType;
    private String description;
    private String photoUrl;
    private Double latitude;
    private Double longitude;
    private String address;
    private ComplaintStatus status;
    private Double vicinityScore;
    private PriorityLevel priorityLevel;
    private UserDTO assignedWorker;
    private LocalDateTime reportedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;
    private String proofPhotoUrl;
    private String resolutionNotes;

    public ComplaintResponseDTO() {
    }

    public static ComplaintResponseDTO fromEntity(Complaint complaint) {
        if (complaint == null) return null;
        ComplaintResponseDTO dto = new ComplaintResponseDTO();
        dto.setId(complaint.getId());
        dto.setCitizen(UserDTO.fromEntity(complaint.getCitizen()));
        dto.setWasteType(complaint.getWasteType());
        dto.setDescription(complaint.getDescription());
        dto.setPhotoUrl(complaint.getPhotoUrl());
        dto.setLatitude(complaint.getLatitude());
        dto.setLongitude(complaint.getLongitude());
        dto.setAddress(complaint.getAddress());
        dto.setStatus(complaint.getStatus());
        dto.setVicinityScore(complaint.getVicinityScore());
        dto.setPriorityLevel(complaint.getPriorityLevel());
        dto.setAssignedWorker(UserDTO.fromEntity(complaint.getAssignedWorker()));
        dto.setReportedAt(complaint.getReportedAt());
        dto.setAssignedAt(complaint.getAssignedAt());
        dto.setResolvedAt(complaint.getResolvedAt());
        dto.setProofPhotoUrl(complaint.getProofPhotoUrl());
        dto.setResolutionNotes(complaint.getResolutionNotes());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getCitizen() {
        return citizen;
    }

    public void setCitizen(UserDTO citizen) {
        this.citizen = citizen;
    }

    public WasteType getWasteType() {
        return wasteType;
    }

    public void setWasteType(WasteType wasteType) {
        this.wasteType = wasteType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public Double getVicinityScore() {
        return vicinityScore;
    }

    public void setVicinityScore(Double vicinityScore) {
        this.vicinityScore = vicinityScore;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public UserDTO getAssignedWorker() {
        return assignedWorker;
    }

    public void setAssignedWorker(UserDTO assignedWorker) {
        this.assignedWorker = assignedWorker;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
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
