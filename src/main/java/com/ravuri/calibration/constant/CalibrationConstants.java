package com.ravuri.calibration.constant;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public interface CalibrationConstants {

    public static final double MIN_ACCEPTABLE_SLOPE = 0.5;
    public static final double MAX_ACCEPTABLE_SLOPE = 1.5;
    public static final double MAX_ACCEPTABLE_OFFSET = 2.0;
    public static final int CALIBRATION_WAIT_TIME = 2000; // milliseconds

    //Bar code width and height
    public static final int WIDTH = 300;
    public static final int HEIGHT = 100;

    //Instrument booking
    public static final Duration MIN_BOOKING_DURATION = Duration.ofMinutes(30);
    public static final Duration MAX_BOOKING_DURATION = Duration.ofHours(8);
    public static final Duration MIN_ADVANCE_BOOKING = Duration.ofHours(1);
    public static final Duration MAX_ADVANCE_BOOKING = Duration.ofDays(30);
    public static final int MAX_ACTIVE_BOOKINGS_PER_USER = 3;

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
}
