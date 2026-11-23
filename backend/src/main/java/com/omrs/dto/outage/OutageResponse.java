package com.omrs.dto.outage;

import java.time.LocalDateTime;

public class OutageResponse {

    private Long id;
    private String outageNumber;
    private String title;
    private String description;
    private Long areaId;
    private String areaName;
    private String areaCode;
    private Long feederId;
    private String feederName;
    private String feederCode;
    private String severity;
    private String status;
    private String outageType;
    private LocalDateTime reportedAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime estimatedRestoreTime;
    private LocalDateTime actualRestoreTime;
    private Long restorationDurationMinutes;
    private Integer affectedCustomersCount;
    private String rootCause;
    private Double latitude;
    private Double longitude;
    private Integer reportCount;
    private String createdByUsername;

    public OutageResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOutageNumber() { return outageNumber; }
    public void setOutageNumber(String outageNumber) { this.outageNumber = outageNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public Long getFeederId() { return feederId; }
    public void setFeederId(Long feederId) { this.feederId = feederId; }
    public String getFeederName() { return feederName; }
    public void setFeederName(String feederName) { this.feederName = feederName; }
    public String getFeederCode() { return feederCode; }
    public void setFeederCode(String feederCode) { this.feederCode = feederCode; }
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
    public String getCreatedByUsername() { return createdByUsername; }
    public void setCreatedByUsername(String createdByUsername) { this.createdByUsername = createdByUsername; }
}
