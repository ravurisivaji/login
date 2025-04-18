package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.CalibrationRecord;
import com.ravuri.calibration.entity.CalibrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalibrationRepository extends JpaRepository<CalibrationRecord, Long> {
    List<CalibrationRecord> findByInstrumentId(String instrumentId);
    Optional<CalibrationRecord> findFirstByInstrumentIdOrderByCalibrationTimeDesc(String instrumentId);
    List<CalibrationRecord> findByNextCalibrationDueBefore(LocalDateTime date);
    List<CalibrationRecord> findByStatus(CalibrationStatus status);
}