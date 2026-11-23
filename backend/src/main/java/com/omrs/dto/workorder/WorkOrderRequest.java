package com.omrs.dto.workorder;

import jakarta.validation.constraints.NotNull;

public class WorkOrderRequest {

    @NotNull(message = "Outage ID is required")
    private Long outageId;

    private Long crewId;
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, EMERGENCY
    private Integer estimatedDurationMinutes = 120;
    private String instructions;

    public WorkOrderRequest() {}

    public Long getOutageId() { return outageId; }
    public void setOutageId(Long outageId) { this.outageId = outageId; }
    public Long getCrewId() { return crewId; }
    public void setCrewId(Long crewId) { this.crewId = crewId; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
