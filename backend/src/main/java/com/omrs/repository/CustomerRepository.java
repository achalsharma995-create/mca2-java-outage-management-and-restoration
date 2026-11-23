package com.omrs.repository;

import com.omrs.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByAccountNumber(String accountNumber);
    Optional<Customer> findByMeterNumber(String meterNumber);
    Optional<Customer> findByUserId(Long userId);
    List<Customer> findByAreaId(Long areaId);
}
