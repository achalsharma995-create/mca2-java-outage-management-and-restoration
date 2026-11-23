package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AREAS")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AREA_ID")
    private Long id;

    @Column(name = "AREA_CODE", length = 20, nullable = false, unique = true)
    private String areaCode;

    @Column(name = "AREA_NAME", length = 100, nullable = false)
    private String areaName;

    @Column(name = "REGION", length = 50, nullable = false)
    private String region;

    @Column(name = "CITY", length = 50, nullable = false)
    private String city;

    @Column(name = "TOTAL_CUSTOMERS", nullable = false)
    private Integer totalCustomers = 0;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Area() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Integer getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(Integer totalCustomers) { this.totalCustomers = totalCustomers; }
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
