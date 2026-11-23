package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WORK_ORDERS")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_ORDER_ID")
    private Long id;

    @Column(name = "WO_NUMBER", length = 50, nullable = false, unique = true)
    private String woNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OUTAGE_ID", nullable = false)
    private Outage outage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CREW_ID")
    private Crew crew;

    @Column(name = "PRIORITY", length = 20, nullable = false)
    private String priority = "MEDIUM";

    @Column(name = "STATUS", length = 20, nullable = false)
    private String status = "CREATED";

    @Column(name = "ASSIGNED_AT")
    private LocalDateTime assignedAt;

    @Column(name = "DISPATCHED_AT")
    private LocalDateTime dispatchedAt;

    @Column(name = "ARRIVED_AT")
    private LocalDateTime arrivedAt;

    @Column(name = "COMPLETED_AT")
    private LocalDateTime completedAt;

    @Column(name = "ESTIMATED_DURATION_MINUTES")
    private Integer estimatedDurationMinutes;

    @Column(name = "INSTRUCTIONS", length = 2000)
    private String instructions;

    @Column(name = "RESOLUTION_NOTES", length = 2000)
    private String resolutionNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", nullable = false)
    private User createdBy;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public WorkOrder() {}

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWoNumber() { return woNumber; }
    public void setWoNumber(String woNumber) { this.woNumber = woNumber; }
    public Outage getOutage() { return outage; }
    public void setOutage(Outage outage) { this.outage = outage; }
    public Crew getCrew() { return crew; }
    public void setCrew(Crew crew) { this.crew = crew; }
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
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
