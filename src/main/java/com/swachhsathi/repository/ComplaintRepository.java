package com.swachhsathi.repository;

import com.swachhsathi.entity.Complaint;
import com.swachhsathi.entity.ComplaintStatus;
import com.swachhsathi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByCitizenOrderByReportedAtDesc(User citizen);

    List<Complaint> findByAssignedWorkerOrderByReportedAtDesc(User worker);

    List<Complaint> findByAssignedWorkerAndStatusOrderByPriorityLevelAscReportedAtDesc(User worker, ComplaintStatus status);

    List<Complaint> findByStatusOrderByReportedAtDesc(ComplaintStatus status);

    long countByStatus(ComplaintStatus status);

    @Query("SELECT c FROM Complaint c WHERE c.assignedWorker.id = :workerId AND c.status IN ('ASSIGNED', 'REPORTED')")
    List<Complaint> findActiveTasksForWorker(@Param("workerId") Long workerId);

    @Query("SELECT c FROM Complaint c ORDER BY c.reportedAt DESC")
    List<Complaint> findAllOrderedByRecent();
}
