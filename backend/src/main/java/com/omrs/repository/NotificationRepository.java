package com.omrs.repository;

import com.omrs.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderBySentAtDesc(Long userId);
    List<Notification> findByCustomerIdOrderBySentAtDesc(Long customerId);
    List<Notification> findByOutageIdOrderBySentAtDesc(Long outageId);
    List<Notification> findAllByOrderBySentAtDesc();
    long countByStatus(String status);
}
