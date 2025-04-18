package com.ravuri.calibration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;

//@Data
@Entity
@Table(name = "chemical_distributions")
public class ChemicalDistribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chemical_id", nullable = false)
    private Chemical chemical;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotBlank(message = "Issued to is required")
    @Column(nullable = false)
    private String issuedTo;

    @NotBlank(message = "Issued by is required")
    @Column(nullable = false)
    private String issuedBy;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Positive(message = "Quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantityIssued;

    private String unit;

    @Column(length = 1000)
    private String purpose;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DistributionStatus status;

    private LocalDateTime returnedAt;

    @Column(name = "quantity_returned")
    private Double quantityReturned;

    @Column(name = "returned_by")
    private String returnedBy;

    @Version
    private Long version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chemical getChemical() {
        return chemical;
    }

    public void setChemical(Chemical chemical) {
        this.chemical = chemical;
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

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Double getQuantityIssued() {
        return quantityIssued;
    }

    public void setQuantityIssued(Double quantityIssued) {
        this.quantityIssued = quantityIssued;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DistributionStatus getStatus() {
        return status;
    }

    public void setStatus(DistributionStatus status) {
        this.status = status;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    public Double getQuantityReturned() {
        return quantityReturned;
    }

    public void setQuantityReturned(Double quantityReturned) {
        this.quantityReturned = quantityReturned;
    }

    public String getReturnedBy() {
        return returnedBy;
    }

    public void setReturnedBy(String returnedBy) {
        this.returnedBy = returnedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ChemicalDistribution{" +
                "id=" + id +
                ", chemical=" + chemical +
                ", department='" + department + '\'' +
                ", location='" + location + '\'' +
                ", issuedTo='" + issuedTo + '\'' +
                ", issuedBy='" + issuedBy + '\'' +
                ", issuedAt=" + issuedAt +
                ", quantityIssued=" + quantityIssued +
                ", unit='" + unit + '\'' +
                ", purpose='" + purpose + '\'' +
                ", notes='" + notes + '\'' +
                ", status=" + status +
                ", returnedAt=" + returnedAt +
                ", quantityReturned=" + quantityReturned +
                ", returnedBy='" + returnedBy + '\'' +
                ", version=" + version +
                '}';
    }
}
