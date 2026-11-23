package com.omrs.dto.outage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OutageReportRequest {

    @NotNull(message = "Area ID is required")
    private Long areaId;

    private Long feederId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String severity = "MEDIUM"; // LOW, MEDIUM, HIGH, CRITICAL

    private String reporterName;
    private String contactPhone;
    private Long customerId;
    private Double latitude;
    private Double longitude;

    public OutageReportRequest() {}

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public Long getFeederId() { return feederId; }
    public void setFeederId(Long feederId) { this.feederId = feederId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
