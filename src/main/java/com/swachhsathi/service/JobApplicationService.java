package com.swachhsathi.service;

import com.swachhsathi.dto.JobApplicationRequestDTO;
import com.swachhsathi.dto.JobApplicationResponseDTO;
import com.swachhsathi.dto.JobApplicationReviewDTO;
import com.swachhsathi.entity.JobApplicationStatus;
import com.swachhsathi.entity.WorkerJobApplication;
import com.swachhsathi.exception.BadRequestException;
import com.swachhsathi.exception.ResourceNotFoundException;
import com.swachhsathi.repository.WorkerJobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final WorkerJobApplicationRepository repository;

    public JobApplicationService(WorkerJobApplicationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public JobApplicationResponseDTO apply(JobApplicationRequestDTO request) {
        if (repository.existsByEmailAndStatus(request.getEmail(), JobApplicationStatus.PENDING)) {
            throw new BadRequestException("A pending application already exists for email: " + request.getEmail());
        }

        WorkerJobApplication app = new WorkerJobApplication();
        app.setApplicantName(request.getApplicantName());
        app.setEmail(request.getEmail());
        app.setPhone(request.getPhone());
        app.setAddress(request.getAddress());
        app.setExperienceYears(request.getExperienceYears());
        app.setIdProofUrl(request.getIdProofUrl());

        return JobApplicationResponseDTO.fromEntity(repository.save(app));
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getAll() {
        return repository.findAllByOrderByAppliedAtDesc().stream()
                .map(JobApplicationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponseDTO> getPending() {
        return repository.findByStatusOrderByAppliedAtDesc(JobApplicationStatus.PENDING).stream()
                .map(JobApplicationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public JobApplicationResponseDTO reviewApplication(Long id, JobApplicationReviewDTO dto) {
        WorkerJobApplication app = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with id: " + id));

        if (app.getStatus() != JobApplicationStatus.PENDING) {
            throw new BadRequestException("Application has already been reviewed. Status: " + app.getStatus());
        }

        if (dto.getStatus() == JobApplicationStatus.PENDING) {
            throw new BadRequestException("Review status must be APPROVED or REJECTED");
        }

        app.setStatus(dto.getStatus());
        app.setAdminNotes(dto.getAdminNotes());
        app.setReviewedAt(LocalDateTime.now());

        return JobApplicationResponseDTO.fromEntity(repository.save(app));
    }
}
