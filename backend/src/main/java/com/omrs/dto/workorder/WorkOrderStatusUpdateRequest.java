package com.omrs.dto.workorder;

import jakarta.validation.constraints.NotBlank;

public class WorkOrderStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status; // ASSIGNED, EN_ROUTE, WORKING, COMPLETED, CANCELLED

    private String resolutionNotes;

    public WorkOrderStatusUpdateRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
}
