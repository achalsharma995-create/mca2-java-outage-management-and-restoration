package com.omrs.repository;

import com.omrs.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Optional<WorkOrder> findByWoNumber(String woNumber);
    List<WorkOrder> findByOutageId(Long outageId);
    List<WorkOrder> findByCrewId(Long crewId);
    List<WorkOrder> findByStatus(String status);
    Optional<WorkOrder> findFirstByOutageIdOrderByCreatedAtDesc(Long outageId);
}
