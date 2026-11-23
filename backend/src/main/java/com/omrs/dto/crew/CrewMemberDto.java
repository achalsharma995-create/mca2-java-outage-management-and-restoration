package com.omrs.dto.crew;

public class CrewMemberDto {

    private Long id;
    private Long userId;
    private String memberName;
    private String roleDesignation;
    private String phone;

    public CrewMemberDto() {}

    public CrewMemberDto(Long id, Long userId, String memberName, String roleDesignation, String phone) {
        this.id = id;
        this.userId = userId;
        this.memberName = memberName;
        this.roleDesignation = roleDesignation;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public String getRoleDesignation() { return roleDesignation; }
    public void setRoleDesignation(String roleDesignation) { this.roleDesignation = roleDesignation; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
