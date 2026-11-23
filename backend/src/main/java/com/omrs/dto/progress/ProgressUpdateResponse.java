package com.omrs.dto.progress;

import java.time.LocalDateTime;

public class ProgressUpdateResponse {

    private Long id;
    private Long workOrderId;
    private String woNumber;
    private Long outageId;
    private String outageNumber;
    private Long engineerId;
    private String engineerName;
    private Integer percentageComplete;
    private String statusReached;
    private String remarks;
    private LocalDateTime estimatedRestorationTime;
    private LocalDateTime createdAt;

    public ProgressUpdateResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    public String getWoNumber() { return woNumber; }
    public void setWoNumber(String woNumber) { this.woNumber = woNumber; }
    public Long getOutageId() { return outageId; }
    public void setOutageId(Long outageId) { this.outageId = outageId; }
    public String getOutageNumber() { return outageNumber; }
    public void setOutageNumber(String outageNumber) { this.outageNumber = outageNumber; }
    public Long getEngineerId() { return engineerId; }
    public void setEngineerId(Long engineerId) { this.engineerId = engineerId; }
    public String getEngineerName() { return engineerName; }
    public void setEngineerName(String engineerName) { this.engineerName = engineerName; }
    public Integer getPercentageComplete() { return percentageComplete; }
    public void setPercentageComplete(Integer percentageComplete) { this.percentageComplete = percentageComplete; }
    public String getStatusReached() { return statusReached; }
    public void setStatusReached(String statusReached) { this.statusReached = statusReached; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDateTime getEstimatedRestorationTime() { return estimatedRestorationTime; }
    public void setEstimatedRestorationTime(LocalDateTime estimatedRestorationTime) { this.estimatedRestorationTime = estimatedRestorationTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
