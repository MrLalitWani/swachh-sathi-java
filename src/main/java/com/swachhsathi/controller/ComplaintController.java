package com.swachhsathi.controller;

import com.swachhsathi.dto.*;
import com.swachhsathi.entity.ComplaintStatus;
import com.swachhsathi.security.UserPrincipal;
import com.swachhsathi.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@Tag(name = "Complaints", description = "Waste complaint reporting and lifecycle management")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    @Operation(summary = "Report a new waste complaint (Citizen)")
    public ResponseEntity<ComplaintResponseDTO> report(
            @Valid @RequestBody ComplaintRequestDTO request,
            @AuthenticationPrincipal UserPrincipal principal) {
        return new ResponseEntity<>(complaintService.reportComplaint(request, principal), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all complaints (Admin/Worker)")
    public ResponseEntity<List<ComplaintResponseDTO>> getAll(
            @RequestParam(required = false) ComplaintStatus status) {
        if (status != null) {
            return ResponseEntity.ok(complaintService.getComplaintsByStatus(status));
        }
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @GetMapping("/my")
    @Operation(summary = "Get authenticated citizen's own complaints")
    public ResponseEntity<List<ComplaintResponseDTO>> getMine(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(complaintService.getMyCitizensComplaints(principal));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a complaint by ID")
    public ResponseEntity<ComplaintResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign a complaint to a worker (Admin only)")
    public ResponseEntity<ComplaintResponseDTO> assign(
            @PathVariable Long id,
            @Valid @RequestBody AssignWorkerDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(complaintService.assignWorker(id, dto, principal));
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Submit resolution proof (Worker only)")
    public ResponseEntity<ComplaintResponseDTO> resolve(
            @PathVariable Long id,
            @Valid @RequestBody ResolveComplaintDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(complaintService.submitResolution(id, dto, principal));
    }

    @PutMapping("/{id}/verify")
    @Operation(summary = "Verify or reject worker resolution (Admin only)")
    public ResponseEntity<ComplaintResponseDTO> verify(
            @PathVariable Long id,
            @Valid @RequestBody VerifyComplaintDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(complaintService.verifyComplaint(id, dto, principal));
    }
}
