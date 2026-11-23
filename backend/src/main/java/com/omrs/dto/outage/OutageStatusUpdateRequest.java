package com.omrs.dto.outage;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class OutageStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status; // REPORTED, VERIFIED, IN_PROGRESS, RESTORED, CANCELLED

    private String rootCause;
    private LocalDateTime estimatedRestoreTime;
    private String resolutionNotes;

    public OutageStatusUpdateRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }
    public LocalDateTime getEstimatedRestoreTime() { return estimatedRestoreTime; }
    public void setEstimatedRestoreTime(LocalDateTime estimatedRestoreTime) { this.estimatedRestoreTime = estimatedRestoreTime; }
    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
}
