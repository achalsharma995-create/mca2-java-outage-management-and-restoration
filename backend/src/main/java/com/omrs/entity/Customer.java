package com.omrs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMERS")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "ACCOUNT_NUMBER", length = 50, nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "FULL_NAME", length = 100, nullable = false)
    private String fullName;

    @Column(name = "PHONE_NUMBER", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "SERVICE_ADDRESS", length = 255, nullable = false)
    private String serviceAddress;

    @Column(name = "METER_NUMBER", length = 50, nullable = false, unique = true)
    private String meterNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_ID", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FEEDER_ID", nullable = false)
    private Feeder feeder;

    @Column(name = "CUSTOMER_TYPE", length = 30, nullable = false)
    private String customerType = "RESIDENTIAL";

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Customer() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getServiceAddress() { return serviceAddress; }
    public void setServiceAddress(String serviceAddress) { this.serviceAddress = serviceAddress; }
    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }
    public Feeder getFeeder() { return feeder; }
    public void setFeeder(Feeder feeder) { this.feeder = feeder; }
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
