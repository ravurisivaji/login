package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.PHBufferReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface PHBufferReadingRepository extends JpaRepository<PHBufferReading, Long> {
    List<PHBufferReading> findByInstrumentId(String instrumentId);
    List<PHBufferReading> findByInstrumentIdAndReadingTimeBetween(
            String instrumentId, LocalDateTime startTime, LocalDateTime endTime);
}