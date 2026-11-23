package com.omrs.dto.progress;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ProgressUpdateRequest {

    @NotNull(message = "Work order ID is required")
    private Long workOrderId;

    @NotNull(message = "Percentage complete is required")
    @Min(value = 0, message = "Percentage must be at least 0")
    @Max(value = 100, message = "Percentage cannot exceed 100")
    private Integer percentageComplete;

    @NotBlank(message = "Status reached is required")
    private String statusReached;

    @NotBlank(message = "Remarks are required")
    private String remarks;

    private LocalDateTime estimatedRestorationTime;

    public ProgressUpdateRequest() {}

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    public Integer getPercentageComplete() { return percentageComplete; }
    public void setPercentageComplete(Integer percentageComplete) { this.percentageComplete = percentageComplete; }
    public String getStatusReached() { return statusReached; }
    public void setStatusReached(String statusReached) { this.statusReached = statusReached; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDateTime getEstimatedRestorationTime() { return estimatedRestorationTime; }
    public void setEstimatedRestorationTime(LocalDateTime estimatedRestorationTime) { this.estimatedRestorationTime = estimatedRestorationTime; }
}
