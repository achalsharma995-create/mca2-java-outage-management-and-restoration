package com.omrs.repository;

import com.omrs.entity.OutageReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutageReportRepository extends JpaRepository<OutageReport, Long> {
    List<OutageReport> findByOutageId(Long outageId);
    List<OutageReport> findByCustomerId(Long customerId);
    List<OutageReport> findByStatus(String status);
}
