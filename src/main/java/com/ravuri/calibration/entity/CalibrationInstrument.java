package com.ravuri.calibration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "calibration_instruments")
public class CalibrationInstrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Serial number is required")
    @Column(unique = true, nullable = false)
    private String serialNumber;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Manufacturer is required")
    @Column(nullable = false)
    private String manufacturer;

    @NotNull(message = "Last calibration date is required")
    @Column(nullable = false)
    private LocalDateTime lastCalibrationDate;

    @NotNull(message = "Next calibration date is required")
    @Column(nullable = false)
    private LocalDateTime nextCalibrationDate;

    @Column(nullable = false)
    private Integer calibrationIntervalDays;

    @Column(nullable = false)
    private Double calibrationAccuracy;

    @Column(length = 1000)
    private String calibrationProcedure;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InstrumentStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @Column(nullable = false)
    private String lastModifiedBy;

    public enum InstrumentStatus {
        ACTIVE,
        IN_CALIBRATION,
        OUT_OF_SERVICE,
        RETIRED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDateTime getLastCalibrationDate() {
        return lastCalibrationDate;
    }

    public void setLastCalibrationDate(LocalDateTime lastCalibrationDate) {
        this.lastCalibrationDate = lastCalibrationDate;
    }

    public LocalDateTime getNextCalibrationDate() {
        return nextCalibrationDate;
    }

    public void setNextCalibrationDate(LocalDateTime nextCalibrationDate) {
        this.nextCalibrationDate = nextCalibrationDate;
    }

    public Integer getCalibrationIntervalDays() {
        return calibrationIntervalDays;
    }

    public void setCalibrationIntervalDays(Integer calibrationIntervalDays) {
        this.calibrationIntervalDays = calibrationIntervalDays;
    }

    public Double getCalibrationAccuracy() {
        return calibrationAccuracy;
    }

    public void setCalibrationAccuracy(Double calibrationAccuracy) {
        this.calibrationAccuracy = calibrationAccuracy;
    }

    public String getCalibrationProcedure() {
        return calibrationProcedure;
    }

    public void setCalibrationProcedure(String calibrationProcedure) {
        this.calibrationProcedure = calibrationProcedure;
    }

    public InstrumentStatus getStatus() {
        return status;
    }

    public void setStatus(InstrumentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public String toString() {
        return "CalibrationInstrument{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", lastCalibrationDate=" + lastCalibrationDate +
                ", nextCalibrationDate=" + nextCalibrationDate +
                ", calibrationIntervalDays=" + calibrationIntervalDays +
                ", calibrationAccuracy=" + calibrationAccuracy +
                ", calibrationProcedure='" + calibrationProcedure + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedAt=" + lastModifiedAt +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                '}';
    }
}
