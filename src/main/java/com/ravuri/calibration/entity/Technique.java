package com.ravuri.calibration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "techniques")
public class Technique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String methodology;

    @ElementCollection
    @CollectionTable(name = "technique_drug_types")
    private Set<String> applicableDrugTypes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "technique_instruments",
            joinColumns = @JoinColumn(name = "technique_id"),
            inverseJoinColumns = @JoinColumn(name = "instrument_id")
    )
    private Set<Instrument> requiredInstruments = new HashSet<>();

    @Column(nullable = false)
    private Double detectionLimitInNanograms;

    @Column(nullable = false)
    private Integer typicalAnalysisTimeInMinutes;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethodology() {
        return methodology;
    }

    public void setMethodology(String methodology) {
        this.methodology = methodology;
    }

    public Set<String> getApplicableDrugTypes() {
        return applicableDrugTypes;
    }

    public void setApplicableDrugTypes(Set<String> applicableDrugTypes) {
        this.applicableDrugTypes = applicableDrugTypes;
    }

    public Set<Instrument> getRequiredInstruments() {
        return requiredInstruments;
    }

    public void setRequiredInstruments(Set<Instrument> requiredInstruments) {
        this.requiredInstruments = requiredInstruments;
    }

    public Double getDetectionLimitInNanograms() {
        return detectionLimitInNanograms;
    }

    public void setDetectionLimitInNanograms(Double detectionLimitInNanograms) {
        this.detectionLimitInNanograms = detectionLimitInNanograms;
    }

    public Integer getTypicalAnalysisTimeInMinutes() {
        return typicalAnalysisTimeInMinutes;
    }

    public void setTypicalAnalysisTimeInMinutes(Integer typicalAnalysisTimeInMinutes) {
        this.typicalAnalysisTimeInMinutes = typicalAnalysisTimeInMinutes;
    }

    @Override
    public String toString() {
        return "Technique{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", methodology='" + methodology + '\'' +
                ", applicableDrugTypes=" + applicableDrugTypes +
                ", requiredInstruments=" + requiredInstruments +
                ", detectionLimitInNanograms=" + detectionLimitInNanograms +
                ", typicalAnalysisTimeInMinutes=" + typicalAnalysisTimeInMinutes +
                '}';
    }
}
