package com.omrs.dto.workorder;

import java.time.LocalDateTime;

public class WorkOrderResponse {

    private Long id;
    private String woNumber;
    private Long outageId;
    private String outageNumber;
    private String outageTitle;
    private String outageSeverity;
    private String outageAreaName;
    private Long crewId;
    private String crewCode;
    private String crewName;
    private String crewVehicleNumber;
    private String priority;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime dispatchedAt;
    private LocalDateTime arrivedAt;
    private LocalDateTime completedAt;
    private Integer estimatedDurationMinutes;
    private String instructions;
    private String resolutionNotes;
    private String createdByUsername;
    private LocalDateTime createdAt;

    public WorkOrderResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWoNumber() { return woNumber; }
    public void setWoNumber(String woNumber) { this.woNumber = woNumber; }
    public Long getOutageId() { return outageId; }
    public void setOutageId(Long outageId) { this.outageId = outageId; }
    public String getOutageNumber() { return outageNumber; }
    public void setOutageNumber(String outageNumber) { this.outageNumber = outageNumber; }
    public String getOutageTitle() { return outageTitle; }
    public void setOutageTitle(String outageTitle) { this.outageTitle = outageTitle; }
    public String getOutageSeverity() { return outageSeverity; }
    public void setOutageSeverity(String outageSeverity) { this.outageSeverity = outageSeverity; }
    public String getOutageAreaName() { return outageAreaName; }
    public void setOutageAreaName(String outageAreaName) { this.outageAreaName = outageAreaName; }
    public Long getCrewId() { return crewId; }
    public void setCrewId(Long crewId) { this.crewId = crewId; }
    public String getCrewCode() { return crewCode; }
    public void setCrewCode(String crewCode) { this.crewCode = crewCode; }
    public String getCrewName() { return crewName; }
    public void setCrewName(String crewName) { this.crewName = crewName; }
    public String getCrewVehicleNumber() { return crewVehicleNumber; }
    public void setCrewVehicleNumber(String crewVehicleNumber) { this.crewVehicleNumber = crewVehicleNumber; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }
    public LocalDateTime getArrivedAt() { return arrivedAt; }
    public void setArrivedAt(LocalDateTime arrivedAt) { this.arrivedAt = arrivedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
    public String getCreatedByUsername() { return createdByUsername; }
    public void setCreatedByUsername(String createdByUsername) { this.createdByUsername = createdByUsername; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
