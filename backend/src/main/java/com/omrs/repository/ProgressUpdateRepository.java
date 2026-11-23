package com.omrs.repository;

import com.omrs.entity.ProgressUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressUpdateRepository extends JpaRepository<ProgressUpdate, Long> {
    List<ProgressUpdate> findByOutageIdOrderByCreatedAtAsc(Long outageId);
    List<ProgressUpdate> findByWorkOrderIdOrderByCreatedAtAsc(Long workOrderId);
}
