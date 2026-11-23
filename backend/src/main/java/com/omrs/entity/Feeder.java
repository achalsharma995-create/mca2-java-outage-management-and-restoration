package com.omrs.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "FEEDERS")
public class Feeder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEEDER_ID")
    private Long id;

    @Column(name = "FEEDER_CODE", length = 50, nullable = false, unique = true)
    private String feederCode;

    @Column(name = "FEEDER_NAME", length = 100, nullable = false)
    private String feederName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_ID", nullable = false)
    private Area area;

    @Column(name = "SUBSTATION_NAME", length = 100, nullable = false)
    private String substationName;

    @Column(name = "CAPACITY_KVA", precision = 10, scale = 2, nullable = false)
    private BigDecimal capacityKva;

    @Column(name = "VOLTAGE_LEVEL", length = 20, nullable = false)
    private String voltageLevel;

    @Column(name = "STATUS", length = 20, nullable = false)
    private String status = "ACTIVE";

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Feeder() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFeederCode() { return feederCode; }
    public void setFeederCode(String feederCode) { this.feederCode = feederCode; }
    public String getFeederName() { return feederName; }
    public void setFeederName(String feederName) { this.feederName = feederName; }
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }
    public String getSubstationName() { return substationName; }
    public void setSubstationName(String substationName) { this.substationName = substationName; }
    public BigDecimal getCapacityKva() { return capacityKva; }
    public void setCapacityKva(BigDecimal capacityKva) { this.capacityKva = capacityKva; }
    public String getVoltageLevel() { return voltageLevel; }
    public void setVoltageLevel(String voltageLevel) { this.voltageLevel = voltageLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
