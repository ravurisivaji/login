package com.ravuri.calibration.controller;

import com.ravuri.calibration.entity.InstrumentSupplier;
import com.ravuri.calibration.entity.SupplierStatus;
import com.ravuri.calibration.service.InstrumentSupplierService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
public class InstrumentSupplierController {

    private static final Logger LOGGER = LogManager.getLogger(InstrumentSupplierController.class);

    @Autowired
    private InstrumentSupplierService supplierService;

    @PostMapping
    public ResponseEntity<InstrumentSupplier> createSupplier(
            @RequestBody InstrumentSupplier supplier,
            @RequestHeader("X-User-Name") String username) {
        LOGGER.info("Creating new supplier: {} by user: {}", supplier.getName(), username);
        return ResponseEntity.ok(supplierService.createSupplier(supplier, username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstrumentSupplier> updateSupplier(
            @PathVariable Long id,
            @RequestBody InstrumentSupplier supplier,
            @RequestHeader("X-User-Name") String username) {
        LOGGER.info("Updating supplier with id: {} by user: {}", id, username);
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplier, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrumentSupplier> getSupplier(@PathVariable Long id) {
        LOGGER.info("Fetching supplier with id: {}", id);
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @GetMapping
    public ResponseEntity<List<InstrumentSupplier>> getAllSuppliers() {
        LOGGER.info("Fetching all suppliers");
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InstrumentSupplier>> getSuppliersByStatus(
            @PathVariable SupplierStatus status) {
        LOGGER.info("Fetching suppliers with status: {}", status);
        return ResponseEntity.ok(supplierService.getSuppliersByStatus(status));
    }

    @GetMapping("/expired-license")
    public ResponseEntity<List<InstrumentSupplier>> getExpiredLicenseSuppliers() {
        LOGGER.info("Fetching suppliers with expired licenses");
        return ResponseEntity.ok(supplierService.getExpiredLicenseSuppliers());
    }

}
