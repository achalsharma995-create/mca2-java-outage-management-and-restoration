package com.omrs.dto.notification;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private Long userId;
    private Long customerId;
    private String customerName;
    private Long outageId;
    private String outageNumber;
    private Long areaId;
    private String areaName;
    private String eventType;
    private String title;
    private String message;
    private String channel;
    private String status;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;

    public NotificationResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public Long getOutageId() { return outageId; }
    public void setOutageId(Long outageId) { this.outageId = outageId; }
    public String getOutageNumber() { return outageNumber; }
    public void setOutageNumber(String outageNumber) { this.outageNumber = outageNumber; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
}
