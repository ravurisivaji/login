package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.email.EmailService;
import com.ravuri.calibration.entity.BookingStatus;
import com.ravuri.calibration.entity.InstrumentBooking;
import com.ravuri.calibration.exception.InstrumentException;
import com.ravuri.calibration.repository.InstrumentBookingRepository;
import com.ravuri.calibration.service.InstrumentBookingService;
import com.ravuri.calibration.service.InstrumentLockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.ravuri.calibration.constant.CalibrationConstants.*;

@Service
public class InstrumentBookingServiceImpl implements InstrumentBookingService {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentBookingServiceImpl.class);

    @Autowired
    private InstrumentBookingRepository bookingRepository;
    @Autowired
    private InstrumentLockService lockService;

    @Autowired
    private EmailService emailService;



    @Override
    @Transactional

    public InstrumentBooking createBooking(InstrumentBooking booking) {
        try {
            validateBookingTimes(booking);
            validateUserBookingLimit(booking.getUserId());
            validateInstrumentAvailability(booking);
            checkForOverlappingBookings(booking);

            booking.setStatus(BookingStatus.PENDING);
            InstrumentBooking savedBooking = bookingRepository.save(booking);

            // Send confirmation email
            //TODO
//            emailService.sendBookingConfirmation(savedBooking, userEmail);

            LOGGER.info("Created booking for instrument {} by user {} from {} to {}",
                    booking.getInstrumentId(), booking.getUserId(),
                    booking.getStartTime(), booking.getEndTime());

            return savedBooking;

        } catch (Exception e) {
            LOGGER.error("Error creating booking", e);
            throw new InstrumentException("Failed to create booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public InstrumentBooking updateBookingTime(Long bookingId, LocalDateTime newStartTime, LocalDateTime newEndTime, String userId) {
        try {
            InstrumentBooking booking = getBookingById(bookingId);
            validateBookingOwnership(booking, userId);
            validateBookingUpdateAllowed(booking);

            // Store old times for logging
            LocalDateTime oldStartTime = booking.getStartTime();
            LocalDateTime oldEndTime = booking.getEndTime();

            // Update times
            booking.setStartTime(newStartTime);
            booking.setEndTime(newEndTime);

            // Validate new times
            validateBookingTimes(booking);
            checkForOverlappingBookings(booking);

            InstrumentBooking updatedBooking = bookingRepository.save(booking);

            LOGGER.info("Updated booking {} times from {}-{} to {}-{}",
                    bookingId, oldStartTime, oldEndTime, newStartTime, newEndTime);

            return updatedBooking;

        } catch (Exception e) {
            LOGGER.error("Error updating booking times", e);
            throw new InstrumentException("Failed to update booking times: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public InstrumentBooking confirmBooking(Long bookingId, String adminUserId) {
        try {
            InstrumentBooking booking = getBookingById(bookingId);
            validateBookingStatus(booking, BookingStatus.PENDING);

            booking.setStatus(BookingStatus.CONFIRMED);
            InstrumentBooking confirmedBooking = bookingRepository.save(booking);

            LOGGER.info("Booking {} confirmed by admin {}", bookingId, adminUserId);
            return confirmedBooking;

        } catch (Exception e) {
            LOGGER.error("Error confirming booking", e);
            throw new InstrumentException("Failed to confirm booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public InstrumentBooking startBooking(Long bookingId, String userId, String portName) {
        try {
            InstrumentBooking booking = getBookingById(bookingId);
            validateBookingStatus(booking, BookingStatus.CONFIRMED);
            validateBookingOwnership(booking, userId);
            validateBookingTime(booking);

            // Lock the instrument
            lockService.lockInstrument(booking.getInstrumentId(), userId, booking.getPurpose(), portName);

            LOGGER.info("Booking {} started by user {}", bookingId, userId);
            return booking;

        } catch (Exception e) {
            LOGGER.error("Error starting booking", e);
            throw new InstrumentException("Failed to start booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public InstrumentBooking completeBooking(Long bookingId, String userId) {
        try {
            InstrumentBooking booking = getBookingById(bookingId);
            validateBookingStatus(booking, BookingStatus.CONFIRMED);
            validateBookingOwnership(booking, userId);

            // Release the instrument lock
            lockService.releaseLock(booking.getInstrumentId(), userId);

            booking.setStatus(BookingStatus.COMPLETED);
            InstrumentBooking completedBooking = bookingRepository.save(booking);

            LOGGER.info("Booking {} completed by user {}", bookingId, userId);
            return completedBooking;

        } catch (Exception e) {
            LOGGER.error("Error completing booking", e);
            throw new InstrumentException("Failed to complete booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public InstrumentBooking cancelBooking(Long bookingId, String userId) {
        try {
            InstrumentBooking booking = getBookingById(bookingId);
            validateBookingOwnership(booking, userId);
            validateCancellation(booking);

            booking.setStatus(BookingStatus.CANCELLED);
            InstrumentBooking cancelledBooking = bookingRepository.save(booking);

            LOGGER.info("Booking {} cancelled by user {}", bookingId, userId);
            return cancelledBooking;

        } catch (Exception e) {
            LOGGER.error("Error cancelling booking", e);
            throw new InstrumentException("Failed to cancel booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentBooking> getBookingsByLocation(String location) {
        return bookingRepository.findByLocationAndStatus(location, BookingStatus.CONFIRMED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstrumentBooking> getBookingsByDepartmentAndLocation(String department, String location) {
        return bookingRepository.findByDepartmentAndLocation(department, location);
    }

    private InstrumentBooking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new InstrumentException("Booking not found with id: " + id));
    }

    private void validateBookingTimes(InstrumentBooking booking) {
        LocalDateTime now = LocalDateTime.now();

        // Basic time validation
        if (booking.getStartTime() == null || booking.getEndTime() == null) {
            throw new InstrumentException("Start time and end time are required");
        }

        if (booking.getEndTime().isBefore(booking.getStartTime())) {
            throw new InstrumentException("End time must be after start time");
        }

        // Validate booking is not too far in the future
        Duration timeUntilStart = Duration.between(now, booking.getStartTime());
        if (timeUntilStart.compareTo(MAX_ADVANCE_BOOKING) > 0) {
            throw new InstrumentException("Cannot book more than " + MAX_ADVANCE_BOOKING.toDays() + " days in advance");
        }

        // Validate minimum advance booking time
        if (timeUntilStart.compareTo(MIN_ADVANCE_BOOKING) < 0) {
            throw new InstrumentException("Bookings must be made at least " + MIN_ADVANCE_BOOKING.toHours() + " hours in advance");
        }

        // Validate booking duration
        Duration duration = Duration.between(booking.getStartTime(), booking.getEndTime());
        if (duration.compareTo(MIN_BOOKING_DURATION) < 0) {
            throw new InstrumentException("Booking duration must be at least " + MIN_BOOKING_DURATION.toMinutes() + " minutes");
        }
        if (duration.compareTo(MAX_BOOKING_DURATION) > 0) {
            throw new InstrumentException("Booking duration cannot exceed " + MAX_BOOKING_DURATION.toHours() + " hours");
        }

        // Validate booking starts and ends on business hours (8 AM to 6 PM)
        if (booking.getStartTime().getHour() < 8 || booking.getStartTime().getHour() >= 18 ||
                booking.getEndTime().getHour() < 8 || booking.getEndTime().getHour() >= 18) {
            throw new InstrumentException("Bookings must be between 8 AM and 6 PM");
        }
    }

    private void validateUserBookingLimit(String userId) {
        List<InstrumentBooking> activeBookings = bookingRepository.findByUserId(userId).stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED ||
                        b.getStatus() == BookingStatus.PENDING)
                .toList();

        if (activeBookings.size() >= MAX_ACTIVE_BOOKINGS_PER_USER) {
            throw new InstrumentException("User has reached the maximum limit of " +
                    MAX_ACTIVE_BOOKINGS_PER_USER + " active bookings");
        }
    }

    private void validateInstrumentAvailability(InstrumentBooking booking) {
        if (lockService.isInstrumentLocked(booking.getInstrumentId())) {
            throw new InstrumentException("Instrument is currently in use");
        }
    }

    private void validateBookingUpdateAllowed(InstrumentBooking booking) {
        if (booking.getStatus() != BookingStatus.PENDING &&
                booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InstrumentException("Cannot update booking in " + booking.getStatus() + " status");
        }

        if (LocalDateTime.now().isAfter(booking.getStartTime())) {
            throw new InstrumentException("Cannot update booking that has already started");
        }
    }

    private void validateLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new InstrumentException("Location is required");
        }
    }

    private void validateDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new InstrumentException("Department is required");
        }
    }

    private void checkForOverlappingBookings(InstrumentBooking booking) {
        List<InstrumentBooking> overlappingBookings = bookingRepository.findOverlappingBookings(
                booking.getInstrumentId(),
                booking.getStartTime(),
                booking.getEndTime()
        );

        if (!overlappingBookings.isEmpty()) {
            throw new InstrumentException("Instrument is already booked for the requested time period");
        }
    }

    private void validateBookingStatus(InstrumentBooking booking, BookingStatus expectedStatus) {
        if (booking.getStatus() != expectedStatus) {
            throw new InstrumentException("Invalid booking status. Expected: " + expectedStatus +
                    ", Current: " + booking.getStatus());
        }
    }

    private void validateBookingOwnership(InstrumentBooking booking, String userId) {
        if (!booking.getUserId().equals(userId)) {
            throw new InstrumentException("Only the booking owner can perform this operation");
        }
    }

    private void validateBookingTime(InstrumentBooking booking) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(booking.getStartTime())) {
            throw new InstrumentException("Cannot start booking before scheduled start time");
        }
        if (now.isAfter(booking.getEndTime())) {
            throw new InstrumentException("Booking has expired");
        }
    }

    private void validateCancellation(InstrumentBooking booking) {
        if (booking.getStatus() == BookingStatus.COMPLETED ||
                booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InstrumentException("Cannot cancel a completed or already cancelled booking");
        }

        if (LocalDateTime.now().isAfter(booking.getStartTime())) {
            throw new InstrumentException("Cannot cancel a booking that has already started");
        }
    }

}
