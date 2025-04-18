package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.InstrumentBooking;
import com.ravuri.calibration.service.InstrumentBookingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/instrument-bookings")
public class InstrumentBookingController {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentBookingController.class);

    @Autowired
    private InstrumentBookingService bookingService;

    @PutMapping("/{bookingId}/time")
    public ResponseEntity<InstrumentBooking> updateBookingTime(
            @PathVariable Long bookingId,
            @RequestParam LocalDateTime newStartTime,
            @RequestParam LocalDateTime newEndTime,
            @RequestHeader("X-User-Name") String userId) {
        return ResponseEntity.ok(bookingService.updateBookingTime(bookingId, newStartTime, newEndTime, userId));
    }

    @PostMapping
    public ResponseEntity<InstrumentBooking> createBooking(
            @RequestBody InstrumentBooking booking,
            @RequestHeader("X-User-Name") String userId) {
        booking.setUserId(userId);
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<InstrumentBooking> confirmBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Name") String adminUserId) {
        return ResponseEntity.ok(bookingService.confirmBooking(bookingId, adminUserId));
    }

    @PostMapping("/{bookingId}/start")
    public ResponseEntity<InstrumentBooking> startBooking(
            @PathVariable Long bookingId,
            @RequestParam String portName,
            @RequestHeader("X-User-Name") String userId) {
        return ResponseEntity.ok(bookingService.startBooking(bookingId, userId, portName));
    }

    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<InstrumentBooking> completeBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Name") String userId) {
        return ResponseEntity.ok(bookingService.completeBooking(bookingId, userId));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<InstrumentBooking> cancelBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Name") String userId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, userId));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<InstrumentBooking>> getBookingsByLocation(
            @PathVariable String location) {
        return ResponseEntity.ok(bookingService.getBookingsByLocation(location));
    }

    @GetMapping("/department/{department}/location/{location}")
    public ResponseEntity<List<InstrumentBooking>> getBookingsByDepartmentAndLocation(
            @PathVariable String department,
            @PathVariable String location) {
        return ResponseEntity.ok(bookingService.getBookingsByDepartmentAndLocation(department, location));
    }

}
