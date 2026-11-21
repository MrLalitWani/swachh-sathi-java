package com.swachhsathi.service;

import com.swachhsathi.dto.*;
import com.swachhsathi.entity.*;
import com.swachhsathi.exception.BadRequestException;
import com.swachhsathi.exception.ResourceNotFoundException;
import com.swachhsathi.repository.*;
import com.swachhsathi.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ComplaintResolutionRepository resolutionRepository;
    private final VicinityPriorityService vicinityPriorityService;

    public ComplaintService(ComplaintRepository complaintRepository,
                            UserRepository userRepository,
                            ComplaintResolutionRepository resolutionRepository,
                            VicinityPriorityService vicinityPriorityService) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.resolutionRepository = resolutionRepository;
        this.vicinityPriorityService = vicinityPriorityService;
    }

    @Transactional
    public ComplaintResponseDTO reportComplaint(ComplaintRequestDTO request, UserPrincipal principal) {
        User citizen = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Calculate vicinity priority score
        VicinityPriorityService.PriorityEvaluation evaluation =
                vicinityPriorityService.evaluatePriority(request.getLatitude(), request.getLongitude(), request.getWasteType());

        Complaint complaint = new Complaint();
        complaint.setCitizen(citizen);
        complaint.setWasteType(request.getWasteType());
        complaint.setDescription(request.getDescription());
        complaint.setLatitude(request.getLatitude());
        complaint.setLongitude(request.getLongitude());
        complaint.setAddress(request.getAddress());
        complaint.setPhotoUrl(request.getPhotoUrl());
        complaint.setVicinityScore(evaluation.getScore());
        complaint.setPriorityLevel(evaluation.getPriorityLevel());
        complaint.setStatus(ComplaintStatus.REPORTED);

        return ComplaintResponseDTO.fromEntity(complaintRepository.save(complaint));
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getAllComplaints() {
        return complaintRepository.findAllOrderedByRecent().stream()
                .map(ComplaintResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComplaintResponseDTO getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));
        return ComplaintResponseDTO.fromEntity(complaint);
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getMyCitizensComplaints(UserPrincipal principal) {
        User citizen = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return complaintRepository.findByCitizenOrderByReportedAtDesc(citizen).stream()
                .map(ComplaintResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComplaintResponseDTO assignWorker(Long complaintId, AssignWorkerDTO dto, UserPrincipal adminPrincipal) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + complaintId));

        if (complaint.getStatus() == ComplaintStatus.RESOLVED) {
            throw new BadRequestException("Cannot re-assign an already resolved complaint");
        }

        User worker = userRepository.findById(dto.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found with id: " + dto.getWorkerId()));

        if (worker.getRole() != Role.ROLE_WORKER) {
            throw new BadRequestException("Assigned user must have ROLE_WORKER role");
        }

        complaint.setAssignedWorker(worker);
        complaint.setStatus(ComplaintStatus.ASSIGNED);
        complaint.setAssignedAt(LocalDateTime.now());

        return ComplaintResponseDTO.fromEntity(complaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponseDTO submitResolution(Long complaintId, ResolveComplaintDTO dto, UserPrincipal workerPrincipal) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + complaintId));

        User worker = userRepository.findById(workerPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        if (complaint.getAssignedWorker() == null || !complaint.getAssignedWorker().getId().equals(worker.getId())) {
            throw new BadRequestException("This complaint is not assigned to you");
        }

        if (complaint.getStatus() != ComplaintStatus.ASSIGNED && complaint.getStatus() != ComplaintStatus.REPORTED) {
            throw new BadRequestException("Complaint is not in a resolvable state: " + complaint.getStatus());
        }

        // Save resolution record
        ComplaintResolution resolution = new ComplaintResolution();
        resolution.setComplaint(complaint);
        resolution.setWorker(worker);
        resolution.setProofPhotoUrl(dto.getProofPhotoUrl());
        resolution.setResolutionNotes(dto.getResolutionNotes());
        resolutionRepository.save(resolution);

        // Update complaint state
        complaint.setStatus(ComplaintStatus.PENDING_VERIFICATION);
        complaint.setProofPhotoUrl(dto.getProofPhotoUrl());
        complaint.setResolutionNotes(dto.getResolutionNotes());

        return ComplaintResponseDTO.fromEntity(complaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponseDTO verifyComplaint(Long complaintId, VerifyComplaintDTO dto, UserPrincipal adminPrincipal) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + complaintId));

        if (complaint.getStatus() != ComplaintStatus.PENDING_VERIFICATION) {
            throw new BadRequestException("Complaint is not awaiting verification. Current status: " + complaint.getStatus());
        }

        User admin = userRepository.findById(adminPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        // Update resolution record
        resolutionRepository.findFirstByComplaintOrderBySubmittedAtDesc(complaint).ifPresent(resolution -> {
            resolution.setApproved(dto.getApproved());
            resolution.setVerifiedByAdmin(admin);
            resolution.setVerifiedAt(LocalDateTime.now());
            resolution.setVerificationFeedback(dto.getFeedback());
            resolutionRepository.save(resolution);
        });

        if (Boolean.TRUE.equals(dto.getApproved())) {
            complaint.setStatus(ComplaintStatus.RESOLVED);
            complaint.setResolvedAt(LocalDateTime.now());
        } else {
            // Rejected: send back to worker
            complaint.setStatus(ComplaintStatus.ASSIGNED);
        }

        return ComplaintResponseDTO.fromEntity(complaintRepository.save(complaint));
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatusOrderByReportedAtDesc(status).stream()
                .map(ComplaintResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
