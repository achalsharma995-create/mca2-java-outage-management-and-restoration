package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREW_MEMBERS")
public class CrewMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREW_ID", nullable = false)
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "MEMBER_NAME", length = 100, nullable = false)
    private String memberName;

    @Column(name = "ROLE_DESIGNATION", length = 50, nullable = false)
    private String roleDesignation;

    @Column(name = "PHONE", length = 20, nullable = false)
    private String phone;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CrewMember() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Crew getCrew() { return crew; }
    public void setCrew(Crew crew) { this.crew = crew; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public String getRoleDesignation() { return roleDesignation; }
    public void setRoleDesignation(String roleDesignation) { this.roleDesignation = roleDesignation; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
