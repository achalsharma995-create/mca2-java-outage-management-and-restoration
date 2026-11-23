package com.omrs.repository;

import com.omrs.entity.Outage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OutageRepository extends JpaRepository<Outage, Long> {

    Optional<Outage> findByOutageNumber(String outageNumber);

    @Query("SELECT o FROM Outage o WHERE " +
           "(:areaId IS NULL OR o.area.id = :areaId) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:severity IS NULL OR o.severity = :severity) AND " +
           "(:searchTerm IS NULL OR LOWER(o.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(o.outageNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Outage> findWithFilters(
            @Param("areaId") Long areaId,
            @Param("status") String status,
            @Param("severity") String severity,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);

    @Query("SELECT o FROM Outage o WHERE o.area.id = :areaId AND o.feeder.id = :feederId AND o.status IN ('REPORTED', 'VERIFIED', 'IN_PROGRESS') ORDER BY o.reportedAt DESC")
    List<Outage> findActiveOutageByAreaAndFeeder(@Param("areaId") Long areaId, @Param("feederId") Long feederId);

    @Query("SELECT o FROM Outage o WHERE o.area.id = :areaId AND o.feeder IS NULL AND o.status IN ('REPORTED', 'VERIFIED', 'IN_PROGRESS') ORDER BY o.reportedAt DESC")
    List<Outage> findActiveOutageByAreaOnly(@Param("areaId") Long areaId);

    long countByStatusNotIn(List<String> statuses);

    @Query("SELECT COUNT(o) FROM Outage o WHERE o.status = 'RESTORED' AND o.actualRestoreTime >= :startOfDay")
    long countRestoredSince(@Param("startOfDay") LocalDateTime startOfDay);
}
