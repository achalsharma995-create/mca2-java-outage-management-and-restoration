package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUTAGE_ID")
    private Outage outage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_ID")
    private Area area;

    @Column(name = "EVENT_TYPE", length = 50, nullable = false)
    private String eventType;

    @Column(name = "TITLE", length = 200, nullable = false)
    private String title;

    @Column(name = "MESSAGE", length = 2000, nullable = false)
    private String message;

    @Column(name = "CHANNEL", length = 20, nullable = false)
    private String channel = "IN_APP";

    @Column(name = "STATUS", length = 20, nullable = false)
    private String status = "SENT";

    @Column(name = "SENT_AT", nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    @Column(name = "READ_AT")
    private LocalDateTime readAt;

    public Notification() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Outage getOutage() { return outage; }
    public void setOutage(Outage outage) { this.outage = outage; }
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }
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
