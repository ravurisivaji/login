package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.InstrumentLock;
import com.ravuri.calibration.service.InstrumentLockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/instrument-locks")
public class InstrumentLockController {
    private static final Logger LOGGER = LogManager.getLogger(InstrumentLockController.class);

    @Autowired
    private InstrumentLockService lockService;

    @PostMapping("/{instrumentId}/lock")
    public ResponseEntity<InstrumentLock> lockInstrument(
            @PathVariable String instrumentId,
            @RequestParam String portName,
            @RequestParam String purpose,
            @RequestHeader("X-User-Name") String userId) {
        LOGGER.info("Locking instrument {} for user {}", instrumentId, userId);
        return ResponseEntity.ok(lockService.lockInstrument(instrumentId, userId, purpose, portName));
    }

    @PostMapping("/{instrumentId}/release")
    public ResponseEntity<InstrumentLock> releaseLock(
            @PathVariable String instrumentId,
            @RequestHeader("X-User-Name") String userId) {
        LOGGER.info("Releasing lock for instrument {} by user {}", instrumentId, userId);
        return ResponseEntity.ok(lockService.releaseLock(instrumentId, userId));
    }

    @PostMapping("/{instrumentId}/force-release")
    public ResponseEntity<InstrumentLock> forceReleaseLock(
            @PathVariable String instrumentId,
            @RequestHeader("X-User-Name") String adminUserId) {
        LOGGER.info("Force releasing lock for instrument {} by admin {}", instrumentId, adminUserId);
        return ResponseEntity.ok(lockService.forceReleaseLock(instrumentId, adminUserId));
    }

    @GetMapping("/{instrumentId}/status")
    public ResponseEntity<Boolean> isInstrumentLocked(@PathVariable String instrumentId) {
        return ResponseEntity.ok(lockService.isInstrumentLocked(instrumentId));
    }

    @GetMapping("/{instrumentId}/active-lock")
    public ResponseEntity<InstrumentLock> getActiveLock(@PathVariable String instrumentId) {
        return ResponseEntity.ok(lockService.getActiveLock(instrumentId));
    }

}
