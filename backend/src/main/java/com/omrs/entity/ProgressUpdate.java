package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PROGRESS_UPDATES")
public class ProgressUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPDATE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUTAGE_ID", nullable = false)
    private Outage outage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ENGINEER_ID", nullable = false)
    private User engineer;

    @Column(name = "PERCENTAGE_COMPLETE", nullable = false)
    private Integer percentageComplete;

    @Column(name = "STATUS_REACHED", length = 50, nullable = false)
    private String statusReached;

    @Column(name = "REMARKS", length = 2000, nullable = false)
    private String remarks;

    @Column(name = "ESTIMATED_RESTORATION_TIME")
    private LocalDateTime estimatedRestorationTime;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProgressUpdate() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }
    public Outage getOutage() { return outage; }
    public void setOutage(Outage outage) { this.outage = outage; }
    public User getEngineer() { return engineer; }
    public void setEngineer(User engineer) { this.engineer = engineer; }
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
