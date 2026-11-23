package com.omrs.dto.crew;

import java.time.LocalDateTime;
import java.util.List;

public class CrewResponse {

    private Long id;
    private String crewCode;
    private String crewName;
    private Long leadEngineerId;
    private String leadEngineerName;
    private String vehicleNumber;
    private String vehicleType;
    private String contactPhone;
    private String availabilityStatus;
    private Long baseAreaId;
    private String baseAreaName;
    private Integer membersCount;
    private String skillSpecialization;
    private List<CrewMemberDto> members;
    private LocalDateTime createdAt;

    public CrewResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCrewCode() { return crewCode; }
    public void setCrewCode(String crewCode) { this.crewCode = crewCode; }
    public String getCrewName() { return crewName; }
    public void setCrewName(String crewName) { this.crewName = crewName; }
    public Long getLeadEngineerId() { return leadEngineerId; }
    public void setLeadEngineerId(Long leadEngineerId) { this.leadEngineerId = leadEngineerId; }
    public String getLeadEngineerName() { return leadEngineerName; }
    public void setLeadEngineerName(String leadEngineerName) { this.leadEngineerName = leadEngineerName; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public Long getBaseAreaId() { return baseAreaId; }
    public void setBaseAreaId(Long baseAreaId) { this.baseAreaId = baseAreaId; }
    public String getBaseAreaName() { return baseAreaName; }
    public void setBaseAreaName(String baseAreaName) { this.baseAreaName = baseAreaName; }
    public Integer getMembersCount() { return membersCount; }
    public void setMembersCount(Integer membersCount) { this.membersCount = membersCount; }
    public String getSkillSpecialization() { return skillSpecialization; }
    public void setSkillSpecialization(String skillSpecialization) { this.skillSpecialization = skillSpecialization; }
    public List<CrewMemberDto> getMembers() { return members; }
    public void setMembers(List<CrewMemberDto> members) { this.members = members; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
