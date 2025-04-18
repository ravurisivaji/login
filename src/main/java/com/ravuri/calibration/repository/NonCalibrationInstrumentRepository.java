package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.NonCalibrationInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NonCalibrationInstrumentRepository extends JpaRepository<NonCalibrationInstrument, Long> {
    Optional<NonCalibrationInstrument> findBySerialNumber(String serialNumber);
    List<NonCalibrationInstrument> findByNextMaintenanceDateBefore(LocalDateTime date);
    List<NonCalibrationInstrument> findByStatus(NonCalibrationInstrument.InstrumentStatus status);
    List<NonCalibrationInstrument> findByManufacturer(String manufacturer);
    boolean existsBySerialNumber(String serialNumber);
}