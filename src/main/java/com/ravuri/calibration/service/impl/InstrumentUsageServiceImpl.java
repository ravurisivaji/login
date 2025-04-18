package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.InstrumentUsage;
import com.ravuri.calibration.entity.UsageStatus;
import com.ravuri.calibration.exception.ConcurrentUsageException;
import com.ravuri.calibration.exception.InstrumentException;
import com.ravuri.calibration.exception.UsageNotFoundException;
import com.ravuri.calibration.exception.UsageValidationException;
import com.ravuri.calibration.repository.InstrumentUsageRepository;
import com.ravuri.calibration.service.InstrumentUsageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InstrumentUsageServiceImpl implements InstrumentUsageService {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentUsageServiceImpl.class);

    @Autowired
    private InstrumentUsageRepository usageRepository;

    @Override
    @Transactional
    public InstrumentUsage startUsage(InstrumentUsage usage) {
        try {
            validateUsageStart(usage);

            List<InstrumentUsage> activeUsages = usageRepository.findActiveUsageByInstrument(usage.getInstrumentId());
            if (!activeUsages.isEmpty()) {
                throw new ConcurrentUsageException("Instrument " + usage.getInstrumentId() + " is already in use");
            }

            usage.setStartTime(LocalDateTime.now());
            usage.setStatus(UsageStatus.IN_PROGRESS);

            InstrumentUsage savedUsage = usageRepository.save(usage);
            LOGGER.info("Started usage for instrument {} by user {}", usage.getInstrumentId(), usage.getUserId());
            return savedUsage;
        } catch (ConcurrentUsageException | UsageValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error starting instrument usage", e);
            throw new InstrumentException("Failed to start instrument usage: " + e.getMessage(), e);
        }
    }

    @Transactional
    public InstrumentUsage endUsage(Long usageId, String notes) {
        try {
            InstrumentUsage usage = usageRepository.findById(usageId)
                    .orElseThrow(() -> new UsageNotFoundException("Usage record not found with id: " + usageId));

            if (usage.getStatus() != UsageStatus.IN_PROGRESS) {
                throw new UsageValidationException("Usage with id " + usageId + " is not in progress");
            }

            LocalDateTime endTime = LocalDateTime.now();
            usage.setEndTime(endTime);
            usage.setDuration(Duration.between(usage.getStartTime(), endTime));
            usage.setStatus(UsageStatus.COMPLETED);
            usage.setNotes(notes);

            InstrumentUsage savedUsage = usageRepository.save(usage);
            LOGGER.info("Ended usage for instrument {} by user {}", usage.getInstrumentId(), usage.getUserId());
            return savedUsage;
        } catch (UsageNotFoundException | UsageValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error ending instrument usage", e);
            throw new InstrumentException("Failed to end instrument usage: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<InstrumentUsage> getUsageByInstrument(String instrumentId) {
        try {
            validateInstrumentId(instrumentId);
            return usageRepository.findByInstrumentId(instrumentId);
        } catch (UsageValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error fetching usage by instrument", e);
            throw new InstrumentException("Failed to fetch instrument usage: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<InstrumentUsage> getUsageByUser(String userId) {
        try {
            validateUserId(userId);
            return usageRepository.findByUserId(userId);
        } catch (UsageValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error fetching usage by user", e);
            throw new InstrumentException("Failed to fetch user usage: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<InstrumentUsage> getUsageByDepartment(String department) {
        try {
            validateDepartment(department);
            return usageRepository.findByDepartment(department);
        } catch (UsageValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error fetching usage by department", e);
            throw new InstrumentException("Failed to fetch department usage: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<InstrumentUsage> getUsageByLocation(String location) {
        try {
            validateLocation(location);
            return usageRepository.findByLocation(location);
        } catch (UsageValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error fetching usage by location", e);
            throw new InstrumentException("Failed to fetch location usage: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<InstrumentUsage> getUsageByTimeRange(LocalDateTime start, LocalDateTime end) {
        try {
            validateTimeRange(start, end);
            return usageRepository.findByStartTimeBetween(start, end);
        } catch (UsageValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error fetching usage by time range", e);
            throw new InstrumentException("Failed to fetch time range usage: " + e.getMessage(), e);
        }
    }

    private void validateUsageStart(InstrumentUsage usage) {
        if (usage == null) {
            throw new UsageValidationException("Usage data cannot be null");
        }
        validateInstrumentId(usage.getInstrumentId());
        validateUserId(usage.getUserId());
        validateDepartment(usage.getDepartment());
        validateLocation(usage.getLocation());
        validatePurpose(usage.getPurpose());
    }

    private void validateInstrumentId(String instrumentId) {
        if (instrumentId == null || instrumentId.trim().isEmpty()) {
            throw new UsageValidationException("Instrument ID is required");
        }
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new UsageValidationException("User ID is required");
        }
    }

    private void validateDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new UsageValidationException("Department is required");
        }
    }

    private void validateLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new UsageValidationException("Location is required");
        }
    }

    private void validatePurpose(String purpose) {
        if (purpose == null || purpose.trim().isEmpty()) {
            throw new UsageValidationException("Purpose is required");
        }
    }

    private void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            throw new UsageValidationException("Start time is required");
        }
        if (end == null) {
            throw new UsageValidationException("End time is required");
        }
        if (end.isBefore(start)) {
            throw new UsageValidationException("End time cannot be before start time");
        }
        if (start.isAfter(LocalDateTime.now())) {
            throw new UsageValidationException("Start time cannot be in the future");
        }
    }



}
