package com.swachhsathi.controller;

import com.swachhsathi.dto.AdminMetricsDTO;
import com.swachhsathi.dto.JobApplicationResponseDTO;
import com.swachhsathi.dto.JobApplicationReviewDTO;
import com.swachhsathi.dto.UserDTO;
import com.swachhsathi.entity.Role;
import com.swachhsathi.repository.UserRepository;
import com.swachhsathi.service.AdminService;
import com.swachhsathi.service.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Admin", description = "Admin KPI dashboard, worker management, and verification")
public class AdminController {

    private final AdminService adminService;
    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;

    public AdminController(AdminService adminService,
                           JobApplicationService jobApplicationService,
                           UserRepository userRepository) {
        this.adminService = adminService;
        this.jobApplicationService = jobApplicationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get system-wide KPI metrics for the admin dashboard")
    public ResponseEntity<AdminMetricsDTO> getMetrics() {
        return ResponseEntity.ok(adminService.getMetrics());
    }

    @GetMapping("/workers")
    @Operation(summary = "List all registered sanitation workers")
    public ResponseEntity<List<UserDTO>> getWorkers() {
        List<UserDTO> workers = userRepository.findByRole(Role.ROLE_WORKER).stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/citizens")
    @Operation(summary = "List all registered citizens")
    public ResponseEntity<List<UserDTO>> getCitizens() {
        List<UserDTO> citizens = userRepository.findByRole(Role.ROLE_CITIZEN).stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citizens);
    }

    @GetMapping("/jobs")
    @Operation(summary = "Get all job applications")
    public ResponseEntity<List<JobApplicationResponseDTO>> getJobApplications(
            @RequestParam(defaultValue = "false") boolean pendingOnly) {
        if (pendingOnly) {
            return ResponseEntity.ok(jobApplicationService.getPending());
        }
        return ResponseEntity.ok(jobApplicationService.getAll());
    }

    @PutMapping("/jobs/{id}/review")
    @Operation(summary = "Approve or reject a job application")
    public ResponseEntity<JobApplicationResponseDTO> reviewJobApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationReviewDTO dto) {
        return ResponseEntity.ok(jobApplicationService.reviewApplication(id, dto));
    }
}
