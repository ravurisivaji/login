package com.ravuri.calibration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

//@Data
@Entity
@Table(name = "chemical_test_cases")
public class ChemicalTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id", nullable = false)
    private TestDocument document;

    @NotBlank(message = "Test case name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String assignedTo;

    @Column(nullable = false)
    private String assignedBy;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TestCaseStatus status;

    @Column(length = 1000)
    private String notes;

    @Version
    private Long version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestDocument getDocument() {
        return document;
    }

    public void setDocument(TestDocument document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public TestCaseStatus getStatus() {
        return status;
    }

    public void setStatus(TestCaseStatus status) {
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

    @Override
    public String toString() {
        return "ChemicalTestCase{" +
                "id=" + id +
                ", document=" + document +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                ", location='" + location + '\'' +
                ", assignedTo='" + assignedTo + '\'' +
                ", assignedBy='" + assignedBy + '\'' +
                ", assignedAt=" + assignedAt +
                ", status=" + status +
                ", notes='" + notes + '\'' +
                ", version=" + version +
                '}';
    }
}
