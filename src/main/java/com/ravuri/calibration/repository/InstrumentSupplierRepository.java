package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.InstrumentSupplier;
import com.ravuri.calibration.entity.SupplierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InstrumentSupplierRepository extends JpaRepository<InstrumentSupplier, Long> {
    List<InstrumentSupplier> findByStatus(SupplierStatus status);
    List<InstrumentSupplier> findByLicenseExpiryDateBefore(LocalDateTime date);
    boolean existsByEmail(String email);
    boolean existsByLicenseNumber(String licenseNumber);
    }