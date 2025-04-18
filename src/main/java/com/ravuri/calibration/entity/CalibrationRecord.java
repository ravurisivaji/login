package com.ravuri.calibration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "calibration_records")
public class CalibrationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String instrumentId;

    @Column(nullable = false)
    private Double standardPhValue;

    @Column(nullable = false)
    private Double measuredPhValue;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double slope;

    @Column(nullable = false)
    private Double offset;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CalibrationStatus status;

    @Column(nullable = false)
    private LocalDateTime calibrationTime;

    @Column(nullable = false)
    private String calibratedBy;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime nextCalibrationDue;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Double getStandardPhValue() {
        return standardPhValue;
    }

    public void setStandardPhValue(Double standardPhValue) {
        this.standardPhValue = standardPhValue;
    }

    public Double getMeasuredPhValue() {
        return measuredPhValue;
    }

    public void setMeasuredPhValue(Double measuredPhValue) {
        this.measuredPhValue = measuredPhValue;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getSlope() {
        return slope;
    }

    public void setSlope(Double slope) {
        this.slope = slope;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    public CalibrationStatus getStatus() {
        return status;
    }

    public void setStatus(CalibrationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCalibrationTime() {
        return calibrationTime;
    }

    public void setCalibrationTime(LocalDateTime calibrationTime) {
        this.calibrationTime = calibrationTime;
    }

    public String getCalibratedBy() {
        return calibratedBy;
    }

    public void setCalibratedBy(String calibratedBy) {
        this.calibratedBy = calibratedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getNextCalibrationDue() {
        return nextCalibrationDue;
    }

    public void setNextCalibrationDue(LocalDateTime nextCalibrationDue) {
        this.nextCalibrationDue = nextCalibrationDue;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "CalibrationRecord{" +
                "id=" + id +
                ", instrumentId='" + instrumentId + '\'' +
                ", standardPhValue=" + standardPhValue +
                ", measuredPhValue=" + measuredPhValue +
                ", temperature=" + temperature +
                ", slope=" + slope +
                ", offset=" + offset +
                ", status=" + status +
                ", calibrationTime=" + calibrationTime +
                ", calibratedBy='" + calibratedBy + '\'' +
                ", notes='" + notes + '\'' +
                ", nextCalibrationDue=" + nextCalibrationDue +
                ", version=" + version +
                '}';
    }
}
