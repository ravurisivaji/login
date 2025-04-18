package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.Chemical;
import com.ravuri.calibration.entity.ChemicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChemicalRepository extends JpaRepository<Chemical, Long> {
    List<Chemical> findByDepartment(String department);
    List<Chemical> findByLocation(String location);
    List<Chemical> findByReceivedBy(String receivedBy);
    List<Chemical> findByStatus(ChemicalStatus status);
    List<Chemical> findByExpirationDateBefore(LocalDateTime date);
    List<Chemical> findByReceivedOnBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByBatchNumber(String batchNumber);
}