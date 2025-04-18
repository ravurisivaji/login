package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.ChemicalDistribution;
import com.ravuri.calibration.entity.DistributionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ChemicalDistributionRepository extends JpaRepository<ChemicalDistribution, Long> {
    List<ChemicalDistribution> findByChemicalId(Long chemicalId);
    List<ChemicalDistribution> findByDepartment(String department);
    List<ChemicalDistribution> findByLocation(String location);
    List<ChemicalDistribution> findByIssuedTo(String issuedTo);
    List<ChemicalDistribution> findByStatus(DistributionStatus status);
    List<ChemicalDistribution> findByIssuedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT cd FROM ChemicalDistribution cd WHERE cd.chemical.id = :chemicalId AND cd.status IN ('ISSUED', 'PARTIALLY_RETURNED')")
    List<ChemicalDistribution> findActiveDistributionsByChemical(@Param("chemicalId") Long chemicalId);
}
//</batchAction>