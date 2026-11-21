package com.swachhsathi.controller;

import com.swachhsathi.dto.ComplaintResponseDTO;
import com.swachhsathi.dto.RoutePlanResponseDTO;
import com.swachhsathi.security.UserPrincipal;
import com.swachhsathi.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/worker")
@PreAuthorize("hasAuthority('ROLE_WORKER')")
@Tag(name = "Worker", description = "Sanitation worker task and route management")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/tasks")
    @Operation(summary = "Get all tasks assigned to the authenticated worker")
    public ResponseEntity<List<ComplaintResponseDTO>> getMyTasks(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(workerService.getMyTasks(principal));
    }

    @GetMapping("/route-plan")
    @Operation(summary = "Get optimized pickup route for today's active tasks")
    public ResponseEntity<RoutePlanResponseDTO> getRoutePlan(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(workerService.getOptimizedRoute(principal));
    }
}
