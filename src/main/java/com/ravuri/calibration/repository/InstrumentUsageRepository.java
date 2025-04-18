package com.ravuri.calibration.repository;


import com.ravuri.calibration.entity.InstrumentUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InstrumentUsageRepository extends JpaRepository<InstrumentUsage, Long> {
    List<InstrumentUsage> findByInstrumentId(String instrumentId);
    List<InstrumentUsage> findByUserId(String userId);
    List<InstrumentUsage> findByDepartment(String department);
    List<InstrumentUsage> findByLocation(String location);

    List<InstrumentUsage> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT u FROM InstrumentUsage u WHERE u.status = 'IN_PROGRESS' AND u.instrumentId = ?1")
    List<InstrumentUsage> findActiveUsageByInstrument(String instrumentId);

    @Query("SELECT u FROM InstrumentUsage u WHERE u.status = 'IN_PROGRESS' AND u.userId = ?1")
    List<InstrumentUsage> findActiveUsageByUser(String userId);
}