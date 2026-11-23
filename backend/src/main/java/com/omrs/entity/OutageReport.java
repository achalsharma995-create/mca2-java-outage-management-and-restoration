package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OUTAGE_REPORTS")
public class OutageReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUTAGE_ID")
    private Outage outage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "REPORTER_NAME", length = 100, nullable = false)
    private String reporterName;

    @Column(name = "CONTACT_PHONE", length = 20, nullable = false)
    private String contactPhone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_ID", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FEEDER_ID")
    private Feeder feeder;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "SEVERITY", length = 20, nullable = false)
    private String severity = "MEDIUM";

    @Column(name = "REPORT_SOURCE", length = 20, nullable = false)
    private String reportSource = "CUSTOMER_PORTAL";

    @Column(name = "REPORTED_AT", nullable = false)
    private LocalDateTime reportedAt = LocalDateTime.now();

    @Column(name = "STATUS", length = 20, nullable = false)
    private String status = "LOGGED";

    public OutageReport() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Outage getOutage() { return outage; }
    public void setOutage(Outage outage) { this.outage = outage; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }
    public Feeder getFeeder() { return feeder; }
    public void setFeeder(Feeder feeder) { this.feeder = feeder; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getReportSource() { return reportSource; }
    public void setReportSource(String reportSource) { this.reportSource = reportSource; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
