package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.InstrumentLock;
import com.ravuri.calibration.entity.LockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentLockRepository extends JpaRepository<InstrumentLock, Long> {
    @Query("SELECT l FROM InstrumentLock l WHERE l.instrumentId = :instrumentId AND l.status = 'ACTIVE'")
    Optional<InstrumentLock> findActiveLock(@Param("instrumentId") String instrumentId);

    List<InstrumentLock> findByInstrumentId(String instrumentId);
    List<InstrumentLock> findByUserId(String userId);
    List<InstrumentLock> findByStatus(LockStatus status);
}