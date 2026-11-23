package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREWS")
public class Crew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREW_ID")
    private Long id;

    @Column(name = "CREW_CODE", length = 30, nullable = false, unique = true)
    private String crewCode;

    @Column(name = "CREW_NAME", length = 100, nullable = false)
    private String crewName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LEAD_ENGINEER_ID")
    private User leadEngineer;

    @Column(name = "VEHICLE_NUMBER", length = 30, nullable = false)
    private String vehicleNumber;

    @Column(name = "VEHICLE_TYPE", length = 50, nullable = false)
    private String vehicleType = "Bucket Truck";

    @Column(name = "CONTACT_PHONE", length = 20, nullable = false)
    private String contactPhone;

    @Column(name = "AVAILABILITY_STATUS", length = 20, nullable = false)
    private String availabilityStatus = "AVAILABLE";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BASE_AREA_ID", nullable = false)
    private Area baseArea;

    @Column(name = "MEMBERS_COUNT", nullable = false)
    private Integer membersCount = 3;

    @Column(name = "SKILL_SPECIALIZATION", length = 100)
    private String skillSpecialization = "High Voltage Restoration";

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public Crew() {}

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCrewCode() { return crewCode; }
    public void setCrewCode(String crewCode) { this.crewCode = crewCode; }
    public String getCrewName() { return crewName; }
    public void setCrewName(String crewName) { this.crewName = crewName; }
    public User getLeadEngineer() { return leadEngineer; }
    public void setLeadEngineer(User leadEngineer) { this.leadEngineer = leadEngineer; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public Area getBaseArea() { return baseArea; }
    public void setBaseArea(Area baseArea) { this.baseArea = baseArea; }
    public Integer getMembersCount() { return membersCount; }
    public void setMembersCount(Integer membersCount) { this.membersCount = membersCount; }
    public String getSkillSpecialization() { return skillSpecialization; }
    public void setSkillSpecialization(String skillSpecialization) { this.skillSpecialization = skillSpecialization; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
