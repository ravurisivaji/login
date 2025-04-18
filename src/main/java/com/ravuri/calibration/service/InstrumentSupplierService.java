package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.InstrumentSupplier;
import com.ravuri.calibration.entity.SupplierStatus;

import java.util.List;

public interface InstrumentSupplierService {
    public InstrumentSupplier createSupplier(InstrumentSupplier supplier, String username);
    public InstrumentSupplier updateSupplier(Long id, InstrumentSupplier supplierDetails, String username);
    public InstrumentSupplier getSupplierById(Long id);
    public List<InstrumentSupplier> getAllSuppliers();
    public List<InstrumentSupplier> getSuppliersByStatus(SupplierStatus status);
    public List<InstrumentSupplier> getExpiredLicenseSuppliers();


}
