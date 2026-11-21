package com.swachhsathi.repository;

import com.swachhsathi.entity.FacilityType;
import com.swachhsathi.entity.SensitiveFacilityPOI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensitiveFacilityPOIRepository extends JpaRepository<SensitiveFacilityPOI, Long> {

    List<SensitiveFacilityPOI> findByFacilityType(FacilityType facilityType);
}
