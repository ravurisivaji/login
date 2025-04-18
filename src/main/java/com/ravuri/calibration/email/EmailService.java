package com.ravuri.calibration.email;

import com.ravuri.calibration.entity.InstrumentBooking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class EmailService {

    private static final Logger LOGGER = LogManager.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void sendBookingConfirmation(InstrumentBooking booking, String userEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userEmail);
            helper.setSubject("Instrument Booking Confirmation");
            helper.setText(createBookingConfirmationEmail(booking), true);

            mailSender.send(message);
            LOGGER.info("Booking confirmation email sent to {}", userEmail);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send booking confirmation email", e);
        }
    }

    public void sendBookingReminder(InstrumentBooking booking, String userEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userEmail);
            helper.setSubject("Instrument Booking Reminder");
            helper.setText(createBookingReminderEmail(booking), true);

            mailSender.send(message);
            LOGGER.info("Booking reminder email sent to {}", userEmail);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send booking reminder email", e);
        }
    }

    private String createBookingConfirmationEmail(InstrumentBooking booking) {
        return String.format("""
            <html>
            <body>
                <h2>Booking Confirmation</h2>
                <p>Your instrument booking has been confirmed with the following details:</p>
                <ul>
                    <li>Instrument ID: %s</li>
                    <li>Start Time: %s</li>
                    <li>End Time: %s</li>
                    <li>Location: %s</li>
                    <li>Department: %s</li>
                    <li>Purpose: %s</li>
                </ul>
                <p>Please arrive on time and follow all instrument usage guidelines.</p>
            </body>
            </html>
            """,
                booking.getInstrumentId(),
                booking.getStartTime().format(formatter),
                booking.getEndTime().format(formatter),
                booking.getLocation(),
                booking.getDepartment(),
                booking.getPurpose()
        );
    }

    private String createBookingReminderEmail(InstrumentBooking booking) {
        return String.format("""
            <html>
            <body>
                <h2>Booking Reminder</h2>
                <p>This is a reminder for your upcoming instrument booking:</p>
                <ul>
                    <li>Instrument ID: %s</li>
                    <li>Start Time: %s</li>
                    <li>End Time: %s</li>
                    <li>Location: %s</li>
                    <li>Department: %s</li>
                </ul>
                <p>Please ensure you arrive on time.</p>
            </body>
            </html>
            """,
                booking.getInstrumentId(),
                booking.getStartTime().format(formatter),
                booking.getEndTime().format(formatter),
                booking.getLocation(),
                booking.getDepartment()
        );
    }

}
