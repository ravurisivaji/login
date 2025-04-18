package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.CalibrationReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalibrationReviewRepository extends JpaRepository<CalibrationReview, Long> {
    Optional<CalibrationReview> findByCalibrationId(Long calibrationId);
    boolean existsByCalibrationId(Long calibrationId);
}