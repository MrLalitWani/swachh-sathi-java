package com.swachhsathi.dto;

public class AdminMetricsDTO {

    private long totalComplaints;
    private long resolvedComplaints;
    private long pendingVerification;
    private long reportedCount;
    private long assignedCount;
    private double averageResolutionDays;
    private long totalWorkers;
    private long totalCitizens;
    private long pendingJobApplications;

    public AdminMetricsDTO() {
    }

    public long getTotalComplaints() {
        return totalComplaints;
    }

    public void setTotalComplaints(long totalComplaints) {
        this.totalComplaints = totalComplaints;
    }

    public long getResolvedComplaints() {
        return resolvedComplaints;
    }

    public void setResolvedComplaints(long resolvedComplaints) {
        this.resolvedComplaints = resolvedComplaints;
    }

    public long getPendingVerification() {
        return pendingVerification;
    }

    public void setPendingVerification(long pendingVerification) {
        this.pendingVerification = pendingVerification;
    }

    public long getReportedCount() {
        return reportedCount;
    }

    public void setReportedCount(long reportedCount) {
        this.reportedCount = reportedCount;
    }

    public long getAssignedCount() {
        return assignedCount;
    }

    public void setAssignedCount(long assignedCount) {
        this.assignedCount = assignedCount;
    }

    public double getAverageResolutionDays() {
        return averageResolutionDays;
    }

    public void setAverageResolutionDays(double averageResolutionDays) {
        this.averageResolutionDays = averageResolutionDays;
    }

    public long getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(long totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public long getTotalCitizens() {
        return totalCitizens;
    }

    public void setTotalCitizens(long totalCitizens) {
        this.totalCitizens = totalCitizens;
    }

    public long getPendingJobApplications() {
        return pendingJobApplications;
    }

    public void setPendingJobApplications(long pendingJobApplications) {
        this.pendingJobApplications = pendingJobApplications;
    }
}
