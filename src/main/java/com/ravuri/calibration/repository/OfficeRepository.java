package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.Office;
import com.ravuri.calibration.entity.OfficeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
    List<Office> findByType(OfficeType type);
    List<Office> findByCountry(String country);
    List<Office> findByState(String state);
    List<Office> findByCity(String city);
}