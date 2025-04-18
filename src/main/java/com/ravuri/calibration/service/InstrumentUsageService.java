package com.ravuri.calibration.service;

import com.ravuri.calibration.entity.InstrumentUsage;
import com.ravuri.calibration.service.impl.InstrumentUsageServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface InstrumentUsageService {
    public InstrumentUsage startUsage(InstrumentUsage usage);
    public InstrumentUsage endUsage(Long usageId, String notes);
    public List<InstrumentUsage> getUsageByInstrument(String instrumentId);
    public List<InstrumentUsage> getUsageByUser(String userId);
    public List<InstrumentUsage> getUsageByDepartment(String department);
    public List<InstrumentUsage> getUsageByLocation(String location);
    public List<InstrumentUsage> getUsageByTimeRange(LocalDateTime start, LocalDateTime end);
    public Map<String, InstrumentUsageServiceImpl.UsageStatistics> generateUserReport(String department, String location);
}
