package com.swachhsathi.service;

import com.swachhsathi.dto.AdminMetricsDTO;
import com.swachhsathi.entity.*;
import com.swachhsathi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
public class AdminService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final WorkerJobApplicationRepository jobApplicationRepository;

    public AdminService(ComplaintRepository complaintRepository,
                        UserRepository userRepository,
                        WorkerJobApplicationRepository jobApplicationRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Transactional(readOnly = true)
    public AdminMetricsDTO getMetrics() {
        AdminMetricsDTO metrics = new AdminMetricsDTO();

        metrics.setTotalComplaints(complaintRepository.count());
        metrics.setResolvedComplaints(complaintRepository.countByStatus(ComplaintStatus.RESOLVED));
        metrics.setPendingVerification(complaintRepository.countByStatus(ComplaintStatus.PENDING_VERIFICATION));
        metrics.setReportedCount(complaintRepository.countByStatus(ComplaintStatus.REPORTED));
        metrics.setAssignedCount(complaintRepository.countByStatus(ComplaintStatus.ASSIGNED));

        metrics.setTotalWorkers(userRepository.findByRole(Role.ROLE_WORKER).size());
        metrics.setTotalCitizens(userRepository.findByRole(Role.ROLE_CITIZEN).size());
        metrics.setPendingJobApplications(
                jobApplicationRepository.findByStatusOrderByAppliedAtDesc(JobApplicationStatus.PENDING).size()
        );

        // Calculate average resolution time in days
        List<Complaint> resolved = complaintRepository.findByStatusOrderByReportedAtDesc(ComplaintStatus.RESOLVED);
        double avgDays = resolved.stream()
                .filter(c -> c.getReportedAt() != null && c.getResolvedAt() != null)
                .mapToLong(c -> Duration.between(c.getReportedAt(), c.getResolvedAt()).toDays())
                .average()
                .orElse(0.0);
        metrics.setAverageResolutionDays(Math.round(avgDays * 10.0) / 10.0);

        return metrics;
    }
}
