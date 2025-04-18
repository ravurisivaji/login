package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.CalibrationInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalibrationInstrumentRepository extends JpaRepository<CalibrationInstrument, Long> {
    Optional<CalibrationInstrument> findBySerialNumber(String serialNumber);
    List<CalibrationInstrument> findByNextCalibrationDateBefore(LocalDateTime date);
    List<CalibrationInstrument> findByStatus(CalibrationInstrument.InstrumentStatus status);
    List<CalibrationInstrument> findByManufacturer(String manufacturer);
    boolean existsBySerialNumber(String serialNumber);
}