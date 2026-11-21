package com.swachhsathi.service;

import com.swachhsathi.entity.PriorityLevel;
import com.swachhsathi.entity.SensitiveFacilityPOI;
import com.swachhsathi.entity.WasteType;
import com.swachhsathi.repository.SensitiveFacilityPOIRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VicinityPriorityService {

    private static final double EARTH_RADIUS_METERS = 6371000.0;
    private static final double SENSITIVITY_RADIUS_METERS = 500.0;

    private final SensitiveFacilityPOIRepository poiRepository;

    public VicinityPriorityService(SensitiveFacilityPOIRepository poiRepository) {
        this.poiRepository = poiRepository;
    }

    public static class PriorityEvaluation {
        private final double score;
        private final PriorityLevel priorityLevel;

        public PriorityEvaluation(double score, PriorityLevel priorityLevel) {
            this.score = score;
            this.priorityLevel = priorityLevel;
        }

        public double getScore() {
            return score;
        }

        public PriorityLevel getPriorityLevel() {
            return priorityLevel;
        }
    }

    public PriorityEvaluation evaluatePriority(double complaintLat, double complaintLng, WasteType wasteType) {
        List<SensitiveFacilityPOI> pois = poiRepository.findAll();

        double highestPoiScore = 1.0;

        for (SensitiveFacilityPOI poi : pois) {
            double distanceMeters = calculateHaversineDistance(
                    complaintLat, complaintLng,
                    poi.getLatitude(), poi.getLongitude()
            );

            if (distanceMeters <= SENSITIVITY_RADIUS_METERS) {
                // Distance decay: closer = higher impact
                double proximityFactor = 1.0 - (distanceMeters / SENSITIVITY_RADIUS_METERS);
                double poiScore = 1.0 + (poi.getWeight() - 1.0) * proximityFactor;
                if (poiScore > highestPoiScore) {
                    highestPoiScore = poiScore;
                }
            }
        }

        // Apply waste category multiplier/boost
        double finalScore = highestPoiScore;
        if (wasteType == WasteType.HAZARDOUS) {
            finalScore += 1.5;
        } else if (wasteType == WasteType.CONSTRUCTION) {
            finalScore += 0.5;
        }

        // Clamp between 1.0 and 5.0
        finalScore = Math.max(1.0, Math.min(5.0, Math.round(finalScore * 10.0) / 10.0));

        PriorityLevel level;
        if (finalScore >= 4.0) {
            level = PriorityLevel.CRITICAL;
        } else if (finalScore >= 3.0) {
            level = PriorityLevel.HIGH;
        } else if (finalScore >= 2.0) {
            level = PriorityLevel.MEDIUM;
        } else if (finalScore >= 1.5) {
            level = PriorityLevel.LOW;
        } else {
            level = PriorityLevel.VERY_LOW;
        }

        return new PriorityEvaluation(finalScore, level);
    }

    public double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return EARTH_RADIUS_METERS * c;
    }
}
