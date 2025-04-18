package com.ravuri.calibration.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

//@Data
@Entity
@Table(name = "calibration_reviews")
public class CalibrationReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "calibration_id", nullable = false)
    private CalibrationRecord calibration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Column(nullable = false)
    private String reviewedBy;

    @Column(nullable = false)
    private LocalDateTime reviewedAt;

    @Column(length = 1000)
    private String comments;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CalibrationRecord getCalibration() {
        return calibration;
    }

    public void setCalibration(CalibrationRecord calibration) {
        this.calibration = calibration;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "CalibrationReview{" +
                "id=" + id +
                ", calibration=" + calibration +
                ", status=" + status +
                ", reviewedBy='" + reviewedBy + '\'' +
                ", reviewedAt=" + reviewedAt +
                ", comments='" + comments + '\'' +
                ", version=" + version +
                '}';
    }
}
