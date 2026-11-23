package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OUTAGES")
public class Outage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OUTAGE_ID")
    private Long id;

    @Column(name = "OUTAGE_NUMBER", length = 50, nullable = false, unique = true)
    private String outageNumber;

    @Column(name = "TITLE", length = 150, nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_ID", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FEEDER_ID")
    private Feeder feeder;

    @Column(name = "SEVERITY", length = 20, nullable = false)
    private String severity = "MEDIUM";

    @Column(name = "STATUS", length = 20, nullable = false)
    private String status = "REPORTED";

    @Column(name = "OUTAGE_TYPE", length = 30, nullable = false)
    private String outageType = "UNPLANNED";

    @Column(name = "REPORTED_AT", nullable = false)
    private LocalDateTime reportedAt = LocalDateTime.now();

    @Column(name = "VERIFIED_AT")
    private LocalDateTime verifiedAt;

    @Column(name = "ESTIMATED_RESTORE_TIME")
    private LocalDateTime estimatedRestoreTime;

    @Column(name = "ACTUAL_RESTORE_TIME")
    private LocalDateTime actualRestoreTime;

    @Column(name = "RESTORATION_DURATION_MINUTES")
    private Long restorationDurationMinutes;

    @Column(name = "AFFECTED_CUSTOMERS_COUNT", nullable = false)
    private Integer affectedCustomersCount = 1;

    @Column(name = "ROOT_CAUSE", length = 255)
    private String rootCause;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "REPORT_COUNT", nullable = false)
    private Integer reportCount = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    private User createdBy;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public Outage() {}

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOutageNumber() { return outageNumber; }
    public void setOutageNumber(String outageNumber) { this.outageNumber = outageNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }
    public Feeder getFeeder() { return feeder; }
    public void setFeeder(Feeder feeder) { this.feeder = feeder; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOutageType() { return outageType; }
    public void setOutageType(String outageType) { this.outageType = outageType; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }
    public LocalDateTime getEstimatedRestoreTime() { return estimatedRestoreTime; }
    public void setEstimatedRestoreTime(LocalDateTime estimatedRestoreTime) { this.estimatedRestoreTime = estimatedRestoreTime; }
    public LocalDateTime getActualRestoreTime() { return actualRestoreTime; }
    public void setActualRestoreTime(LocalDateTime actualRestoreTime) { this.actualRestoreTime = actualRestoreTime; }
    public Long getRestorationDurationMinutes() { return restorationDurationMinutes; }
    public void setRestorationDurationMinutes(Long restorationDurationMinutes) { this.restorationDurationMinutes = restorationDurationMinutes; }
    public Integer getAffectedCustomersCount() { return affectedCustomersCount; }
    public void setAffectedCustomersCount(Integer affectedCustomersCount) { this.affectedCustomersCount = affectedCustomersCount; }
    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Integer getReportCount() { return reportCount; }
    public void setReportCount(Integer reportCount) { this.reportCount = reportCount; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
