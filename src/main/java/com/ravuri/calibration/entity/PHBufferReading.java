package com.ravuri.calibration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

//@Data
@Entity
@Table(name = "ph_buffer_readings")
public class PHBufferReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String instrumentId;

    @Column(nullable = false)
    private Double phValue;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double conductivity;

    @Column(nullable = false)
    private LocalDateTime readingTime;

    @Column(nullable = false)
    private String readBy;

    @Column(nullable = false)
    private LocalDateTime readAt;

    @Column(nullable = false)
    private String status;

    @Column(length = 1000)
    private String notes;

    @Version
    private Long version;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @Column(nullable = false)
    private String lastModifiedBy;

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

    public Double getPhValue() {
        return phValue;
    }

    public void setPhValue(Double phValue) {
        this.phValue = phValue;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getConductivity() {
        return conductivity;
    }

    public void setConductivity(Double conductivity) {
        this.conductivity = conductivity;
    }

    public LocalDateTime getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(LocalDateTime readingTime) {
        this.readingTime = readingTime;
    }

    public String getReadBy() {
        return readBy;
    }

    public void setReadBy(String readBy) {
        this.readBy = readBy;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
        return "PHBufferReading{" +
                "id=" + id +
                ", instrumentId='" + instrumentId + '\'' +
                ", phValue=" + phValue +
                ", temperature=" + temperature +
                ", conductivity=" + conductivity +
                ", readingTime=" + readingTime +
                ", readBy='" + readBy + '\'' +
                ", readAt=" + readAt +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedAt=" + lastModifiedAt +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                '}';
    }
}
