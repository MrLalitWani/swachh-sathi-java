package com.swachhsathi.service;

import com.swachhsathi.dto.ComplaintResponseDTO;
import com.swachhsathi.dto.RoutePlanResponseDTO;
import com.swachhsathi.entity.Complaint;
import com.swachhsathi.entity.ComplaintStatus;
import com.swachhsathi.entity.User;
import com.swachhsathi.exception.ResourceNotFoundException;
import com.swachhsathi.repository.ComplaintRepository;
import com.swachhsathi.repository.UserRepository;
import com.swachhsathi.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final VicinityPriorityService vicinityPriorityService;

    public WorkerService(ComplaintRepository complaintRepository,
                         UserRepository userRepository,
                         VicinityPriorityService vicinityPriorityService) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.vicinityPriorityService = vicinityPriorityService;
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDTO> getMyTasks(UserPrincipal principal) {
        User worker = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
        return complaintRepository.findByAssignedWorkerOrderByReportedAtDesc(worker).stream()
                .map(ComplaintResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Greedy nearest-neighbor route optimization.
     * Returns complaints ordered by shortest-travel sequence starting from the
     * highest-priority unresolved task.
     */
    @Transactional(readOnly = true)
    public RoutePlanResponseDTO getOptimizedRoute(UserPrincipal principal) {
        User worker = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        List<Complaint> activeTasks = complaintRepository.findActiveTasksForWorker(worker.getId());

        List<Complaint> ordered = new ArrayList<>();
        List<Complaint> remaining = new ArrayList<>(activeTasks);

        double totalDistanceMeters = 0;

        if (!remaining.isEmpty()) {
            // Start from CRITICAL or highest-priority complaint
            remaining.sort((a, b) -> {
                if (a.getPriorityLevel() == null) return 1;
                if (b.getPriorityLevel() == null) return -1;
                return a.getPriorityLevel().compareTo(b.getPriorityLevel());
            });

            Complaint current = remaining.remove(0);
            ordered.add(current);

            while (!remaining.isEmpty()) {
                final double curLat = current.getLatitude();
                final double curLng = current.getLongitude();

                // Pick nearest neighbor
                Complaint nearest = null;
                double minDist = Double.MAX_VALUE;

                for (Complaint candidate : remaining) {
                    double dist = vicinityPriorityService.calculateHaversineDistance(
                            curLat, curLng, candidate.getLatitude(), candidate.getLongitude()
                    );
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = candidate;
                    }
                }

                if (nearest != null) {
                    totalDistanceMeters += minDist;
                    ordered.add(nearest);
                    remaining.remove(nearest);
                    current = nearest;
                }
            }
        }

        List<ComplaintResponseDTO> stops = ordered.stream()
                .map(ComplaintResponseDTO::fromEntity)
                .collect(Collectors.toList());

        double totalDistanceKm = Math.round((totalDistanceMeters / 1000.0) * 10.0) / 10.0;

        return new RoutePlanResponseDTO(
                worker.getId(),
                worker.getName(),
                stops.size(),
                totalDistanceKm,
                stops
        );
    }
}
