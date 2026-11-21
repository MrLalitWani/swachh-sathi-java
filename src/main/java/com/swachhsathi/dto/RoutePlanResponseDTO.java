package com.swachhsathi.dto;

import java.util.List;

public class RoutePlanResponseDTO {

    private Long workerId;
    private String workerName;
    private int totalStops;
    private double estimatedTotalDistanceKm;
    private List<ComplaintResponseDTO> stops;

    public RoutePlanResponseDTO() {
    }

    public RoutePlanResponseDTO(Long workerId, String workerName, int totalStops, double estimatedTotalDistanceKm, List<ComplaintResponseDTO> stops) {
        this.workerId = workerId;
        this.workerName = workerName;
        this.totalStops = totalStops;
        this.estimatedTotalDistanceKm = estimatedTotalDistanceKm;
        this.stops = stops;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public int getTotalStops() {
        return totalStops;
    }

    public void setTotalStops(int totalStops) {
        this.totalStops = totalStops;
    }

    public double getEstimatedTotalDistanceKm() {
        return estimatedTotalDistanceKm;
    }

    public void setEstimatedTotalDistanceKm(double estimatedTotalDistanceKm) {
        this.estimatedTotalDistanceKm = estimatedTotalDistanceKm;
    }

    public List<ComplaintResponseDTO> getStops() {
        return stops;
    }

    public void setStops(List<ComplaintResponseDTO> stops) {
        this.stops = stops;
    }
}
