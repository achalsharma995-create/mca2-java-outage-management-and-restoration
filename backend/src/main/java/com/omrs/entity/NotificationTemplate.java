package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION_TEMPLATES")
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMPLATE_ID")
    private Long id;

    @Column(name = "EVENT_CODE", length = 50, nullable = false, unique = true)
    private String eventCode;

    @Column(name = "EVENT_NAME", length = 100, nullable = false)
    private String eventName;

    @Column(name = "TITLE_TEMPLATE", length = 200, nullable = false)
    private String titleTemplate;

    @Column(name = "BODY_TEMPLATE", length = 1000, nullable = false)
    private String bodyTemplate;

    @Column(name = "CHANNELS", length = 50, nullable = false)
    private String channels = "SMS,EMAIL,IN_APP";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public NotificationTemplate() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventCode() { return eventCode; }
    public void setEventCode(String eventCode) { this.eventCode = eventCode; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getTitleTemplate() { return titleTemplate; }
    public void setTitleTemplate(String titleTemplate) { this.titleTemplate = titleTemplate; }
    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }
    public String getChannels() { return channels; }
    public void setChannels(String channels) { this.channels = channels; }
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
