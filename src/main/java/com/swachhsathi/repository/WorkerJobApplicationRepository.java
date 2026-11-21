package com.swachhsathi.repository;

import com.swachhsathi.entity.JobApplicationStatus;
import com.swachhsathi.entity.WorkerJobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerJobApplicationRepository extends JpaRepository<WorkerJobApplication, Long> {

    List<WorkerJobApplication> findByStatusOrderByAppliedAtDesc(JobApplicationStatus status);

    List<WorkerJobApplication> findAllByOrderByAppliedAtDesc();

    boolean existsByEmailAndStatus(String email, JobApplicationStatus status);
}
