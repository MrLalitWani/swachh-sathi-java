package com.swachhsathi.controller;

import com.swachhsathi.dto.JobApplicationRequestDTO;
import com.swachhsathi.dto.JobApplicationResponseDTO;
import com.swachhsathi.service.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Applications", description = "Public citizen job application to become a sanitation worker")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/apply")
    @Operation(summary = "Submit a sanitation worker job application (public, no auth required)")
    public ResponseEntity<JobApplicationResponseDTO> apply(@Valid @RequestBody JobApplicationRequestDTO request) {
        return new ResponseEntity<>(jobApplicationService.apply(request), HttpStatus.CREATED);
    }
}
