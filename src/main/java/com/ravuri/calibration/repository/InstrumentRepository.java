package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ravuri.calibration.entity.InstrumentStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    List<Instrument> findByStatus(InstrumentStatus status);
    List<Instrument> findByNextCalibrationDateBefore(LocalDate date);
    List<Instrument> findByManufacturer(String manufacturer);
    boolean existsBySerialNumber(String serialNumber);
}