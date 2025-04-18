package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.Technique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TechniqueRepository extends JpaRepository<Technique, Long> {
    List<Technique> findByApplicableDrugTypesContaining(String drugType);
    boolean existsByName(String name);
}