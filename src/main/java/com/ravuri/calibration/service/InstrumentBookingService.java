package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.InstrumentBooking;

import java.time.LocalDateTime;
import java.util.List;

public interface InstrumentBookingService {
    public InstrumentBooking createBooking(InstrumentBooking booking);
    public InstrumentBooking confirmBooking(Long bookingId, String adminUserId);
    public InstrumentBooking startBooking(Long bookingId, String userId, String portName);
    public InstrumentBooking completeBooking(Long bookingId, String userId);
    public InstrumentBooking cancelBooking(Long bookingId, String userId);
    public List<InstrumentBooking> getBookingsByLocation(String location);
    public List<InstrumentBooking> getBookingsByDepartmentAndLocation(String department, String location);
    public InstrumentBooking updateBookingTime(Long bookingId, LocalDateTime newStartTime, LocalDateTime newEndTime, String userId);
}
