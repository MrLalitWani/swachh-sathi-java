package com.swachhsathi.repository;

import com.swachhsathi.entity.Complaint;
import com.swachhsathi.entity.ComplaintResolution;
import com.swachhsathi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintResolutionRepository extends JpaRepository<ComplaintResolution, Long> {

    List<ComplaintResolution> findByComplaintOrderBySubmittedAtDesc(Complaint complaint);

    List<ComplaintResolution> findByWorkerOrderBySubmittedAtDesc(User worker);

    Optional<ComplaintResolution> findFirstByComplaintOrderBySubmittedAtDesc(Complaint complaint);
}
