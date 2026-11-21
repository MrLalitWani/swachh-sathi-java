package com.swachhsathi;

import com.swachhsathi.service.VicinityPriorityService;
import com.swachhsathi.entity.FacilityType;
import com.swachhsathi.entity.PriorityLevel;
import com.swachhsathi.entity.SensitiveFacilityPOI;
import com.swachhsathi.entity.WasteType;
import com.swachhsathi.repository.SensitiveFacilityPOIRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class VicinityPriorityServiceTest {

    @Mock
    private SensitiveFacilityPOIRepository poiRepository;

    @InjectMocks
    private VicinityPriorityService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void haversineDistance_samePoint_returnsZero() {
        double dist = service.calculateHaversineDistance(18.52, 73.86, 18.52, 73.86);
        assertThat(dist).isLessThan(0.001);
    }

    @Test
    void haversineDistance_knownPoints_isAccurate() {
        // Ruby Hall Clinic (18.5362, 73.8731) to KEM Hospital (18.5195, 73.8553)
        // Haversine distance is approximately 2640m
        double dist = service.calculateHaversineDistance(18.5362, 73.8731, 18.5195, 73.8553);
        assertThat(dist).isBetween(2400.0, 2900.0);
    }

    @Test
    void evaluatePriority_nearHospital_returnsHighPriority() {
        SensitiveFacilityPOI hospital = new SensitiveFacilityPOI();
        hospital.setId(1L);
        hospital.setName("Ruby Hall Clinic");
        hospital.setFacilityType(FacilityType.HOSPITAL);
        hospital.setLatitude(18.5362);
        hospital.setLongitude(73.8731);
        hospital.setWeight(4.5);

        when(poiRepository.findAll()).thenReturn(List.of(hospital));

        // Complaint location 100m from Ruby Hall
        VicinityPriorityService.PriorityEvaluation result =
                service.evaluatePriority(18.5368, 73.8740, WasteType.GENERAL);

        assertThat(result.getScore()).isGreaterThan(2.0);
        assertThat(result.getPriorityLevel()).isIn(PriorityLevel.HIGH, PriorityLevel.CRITICAL);
    }

    @Test
    void evaluatePriority_farFromAll_returnsVeryLow() {
        when(poiRepository.findAll()).thenReturn(List.of());

        VicinityPriorityService.PriorityEvaluation result =
                service.evaluatePriority(18.0000, 73.0000, WasteType.GENERAL);

        assertThat(result.getScore()).isEqualTo(1.0);
        assertThat(result.getPriorityLevel()).isEqualTo(PriorityLevel.VERY_LOW);
    }

    @Test
    void evaluatePriority_hazardousWaste_boostedScore() {
        when(poiRepository.findAll()).thenReturn(List.of());

        VicinityPriorityService.PriorityEvaluation result =
                service.evaluatePriority(18.0000, 73.0000, WasteType.HAZARDOUS);

        // No POIs: base score = 1.0 + HAZARDOUS boost 1.5 = 2.5 → MEDIUM (threshold: HIGH >= 3.0)
        assertThat(result.getScore()).isEqualTo(2.5);
        assertThat(result.getPriorityLevel()).isEqualTo(PriorityLevel.MEDIUM);
    }
}
