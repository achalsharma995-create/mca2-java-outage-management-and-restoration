package com.omrs.dto.crew;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CrewRequest {

    @NotBlank(message = "Crew code is required")
    private String crewCode;

    @NotBlank(message = "Crew name is required")
    private String crewName;

    private Long leadEngineerId;

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    private String vehicleType = "Bucket Truck";

    @NotBlank(message = "Contact phone is required")
    private String contactPhone;

    private String availabilityStatus = "AVAILABLE";

    @NotNull(message = "Base area ID is required")
    private Long baseAreaId;

    private Integer membersCount = 3;
    private String skillSpecialization;
    private List<CrewMemberDto> members;

    public CrewRequest() {}

    public String getCrewCode() { return crewCode; }
    public void setCrewCode(String crewCode) { this.crewCode = crewCode; }
    public String getCrewName() { return crewName; }
    public void setCrewName(String crewName) { this.crewName = crewName; }
    public Long getLeadEngineerId() { return leadEngineerId; }
    public void setLeadEngineerId(Long leadEngineerId) { this.leadEngineerId = leadEngineerId; }
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
    public Integer getMembersCount() { return membersCount; }
    public void setMembersCount(Integer membersCount) { this.membersCount = membersCount; }
    public String getSkillSpecialization() { return skillSpecialization; }
    public void setSkillSpecialization(String skillSpecialization) { this.skillSpecialization = skillSpecialization; }
    public List<CrewMemberDto> getMembers() { return members; }
    public void setMembers(List<CrewMemberDto> members) { this.members = members; }
}
