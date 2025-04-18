package com.ravuri.calibration.repository;

import com.ravuri.calibration.entity.BookingStatus;
import com.ravuri.calibration.entity.InstrumentBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InstrumentBookingRepository extends JpaRepository<InstrumentBooking, Long> {
    @Query("SELECT b FROM InstrumentBooking b WHERE b.instrumentId = :instrumentId " +
            "AND b.status IN ('CONFIRMED', 'PENDING') " +
            "AND ((b.startTime BETWEEN :start AND :end) OR " +
            "(b.endTime BETWEEN :start AND :end) OR " +
            "(b.startTime <= :start AND b.endTime >= :end))")
    List<InstrumentBooking> findOverlappingBookings(
            @Param("instrumentId") String instrumentId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<InstrumentBooking> findByLocationAndStatus(String location, BookingStatus status);
    List<InstrumentBooking> findByDepartmentAndLocation(String department, String location);
    List<InstrumentBooking> findByUserId(String userId);
    List<InstrumentBooking> findByInstrumentId(String instrumentId);
}