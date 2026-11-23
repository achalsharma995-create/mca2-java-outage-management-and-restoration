package com.omrs.service;

import com.omrs.dto.dashboard.DashboardMetricsDto;
import com.omrs.entity.Outage;
import com.omrs.repository.CrewRepository;
import com.omrs.repository.NotificationRepository;
import com.omrs.repository.OutageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OutageRepository outageRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardMetricsDto getDashboardMetrics() {
        List<String> closedStatuses = Arrays.asList("RESTORED", "CANCELLED");
        long activeOutages = outageRepository.countByStatusNotIn(closedStatuses);

        long availableCrews = crewRepository.countByAvailabilityStatus("AVAILABLE");
        long totalCrews = crewRepository.count();

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        long restoredToday = outageRepository.countRestoredSince(startOfToday);

        long pendingNotifications = notificationRepository.countByStatus("QUEUED");

        // Sum affected customers for all active outages
        List<Outage> allOutages = outageRepository.findAll();
        long totalAffectedCustomers = allOutages.stream()
                .filter(o -> !"RESTORED".equals(o.getStatus()) && !"CANCELLED".equals(o.getStatus()))
                .mapToLong(o -> o.getAffectedCustomersCount() != null ? o.getAffectedCustomersCount() : 0)
                .sum();

        return new DashboardMetricsDto(
                activeOutages,
                availableCrews,
                totalCrews,
                restoredToday,
                pendingNotifications,
                totalAffectedCustomers
        );
    }
}
