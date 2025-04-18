package com.ravuri.calibration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

//@Data
@Entity
@Table(name = "chemicals")
public class Chemical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Chemical name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Batch number is required")
    @Column(nullable = false)
    private String batchNumber;

    @NotBlank(message = "CAS number is required")
    @Column(nullable = false)
    private String casNumber;

    @NotNull(message = "Received date is required")
    @Column(nullable = false)
    private LocalDateTime receivedOn;

    @NotNull(message = "Expiration date is required")
    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotBlank(message = "Received by is required")
    @Column(nullable = false)
    private String receivedBy;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChemicalStatus status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public LocalDateTime getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(LocalDateTime receivedOn) {
        this.receivedOn = receivedOn;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ChemicalStatus getStatus() {
        return status;
    }

    public void setStatus(ChemicalStatus status) {
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
        return "Chemical{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", casNumber='" + casNumber + '\'' +
                ", receivedOn=" + receivedOn +
                ", expirationDate=" + expirationDate +
                ", department='" + department + '\'' +
                ", location='" + location + '\'' +
                ", receivedBy='" + receivedBy + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", status=" + status +
                ", notes='" + notes + '\'' +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedAt=" + lastModifiedAt +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                '}';
    }
}
