package com.omrs.service;

import com.omrs.dto.outage.OutageReportRequest;
import com.omrs.dto.outage.OutageResponse;
import com.omrs.dto.outage.OutageStatusUpdateRequest;
import com.omrs.entity.*;
import com.omrs.exception.ResourceNotFoundException;
import com.omrs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OutageServiceImpl implements OutageService {

    @Autowired
    private OutageRepository outageRepository;

    @Autowired
    private OutageReportRepository outageReportRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private FeederRepository feederRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public OutageResponse reportOutage(OutageReportRequest request, Long reporterUserId) {
        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Area", "id", request.getAreaId()));

        Feeder feeder = null;
        if (request.getFeederId() != null) {
            feeder = feederRepository.findById(request.getFeederId())
                    .orElseThrow(() -> new ResourceNotFoundException("Feeder", "id", request.getFeederId()));
        }

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        }

        User user = null;
        if (reporterUserId != null) {
            user = userRepository.findById(reporterUserId).orElse(null);
        }

        // Duplicate grouping check for active outages in the same area / feeder
        List<Outage> existingActiveOutages;
        if (feeder != null) {
            existingActiveOutages = outageRepository.findActiveOutageByAreaAndFeeder(area.getId(), feeder.getId());
        } else {
            existingActiveOutages = outageRepository.findActiveOutageByAreaOnly(area.getId());
        }

        Outage targetOutage;
        if (!existingActiveOutages.isEmpty()) {
            // Group duplicate into the existing active incident
            targetOutage = existingActiveOutages.get(0);
            targetOutage.setReportCount(targetOutage.getReportCount() + 1);
            targetOutage.setAffectedCustomersCount(targetOutage.getAffectedCustomersCount() + 1);
            outageRepository.save(targetOutage);

            OutageReport subReport = new OutageReport();
            subReport.setOutage(targetOutage);
            subReport.setCustomer(customer);
            subReport.setArea(area);
            subReport.setFeeder(feeder);
            subReport.setReporterName(request.getReporterName() != null ? request.getReporterName() : (user != null ? user.getFullName() : "Anonymous"));
            subReport.setContactPhone(request.getContactPhone() != null ? request.getContactPhone() : (user != null ? user.getPhoneNumber() : "N/A"));
            subReport.setDescription(request.getDescription());
            subReport.setSeverity(request.getSeverity() != null ? request.getSeverity() : "MEDIUM");
            subReport.setStatus("LINKED_TO_OUTAGE");
            outageReportRepository.save(subReport);

            return mapToResponse(targetOutage);
        }

        // New Incident creation
        targetOutage = new Outage();
        String generatedOutageNumber = "OUT-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        targetOutage.setOutageNumber(generatedOutageNumber);
        targetOutage.setTitle(request.getTitle());
        targetOutage.setDescription(request.getDescription());
        targetOutage.setArea(area);
        targetOutage.setFeeder(feeder);
        targetOutage.setSeverity(request.getSeverity() != null ? request.getSeverity() : "MEDIUM");
        targetOutage.setStatus("REPORTED");
        targetOutage.setOutageType("UNPLANNED");
        targetOutage.setReportedAt(LocalDateTime.now());
        targetOutage.setAffectedCustomersCount(1);
        targetOutage.setReportCount(1);
        targetOutage.setLatitude(request.getLatitude());
        targetOutage.setLongitude(request.getLongitude());
        targetOutage.setCreatedBy(user);

        Outage savedOutage = outageRepository.save(targetOutage);

        // Also record the individual report audit
        OutageReport initialReport = new OutageReport();
        initialReport.setOutage(savedOutage);
        initialReport.setCustomer(customer);
        initialReport.setArea(area);
        initialReport.setFeeder(feeder);
        initialReport.setReporterName(request.getReporterName() != null ? request.getReporterName() : (user != null ? user.getFullName() : "System Operator"));
        initialReport.setContactPhone(request.getContactPhone() != null ? request.getContactPhone() : (user != null ? user.getPhoneNumber() : "N/A"));
        initialReport.setDescription(request.getDescription());
        initialReport.setSeverity(savedOutage.getSeverity());
        initialReport.setStatus("LINKED_TO_OUTAGE");
        outageReportRepository.save(initialReport);

        // Trigger Notification
        notificationService.triggerOutageNotification(savedOutage, "OUTAGE_REPORTED");

        return mapToResponse(savedOutage);
    }

    @Override
    @Transactional(readOnly = true)
    public OutageResponse getOutageById(Long outageId) {
        Outage outage = outageRepository.findById(outageId)
                .orElseThrow(() -> new ResourceNotFoundException("Outage", "id", outageId));
        return mapToResponse(outage);
    }

    @Override
    @Transactional(readOnly = true)
    public OutageResponse getOutageByNumber(String outageNumber) {
        Outage outage = outageRepository.findByOutageNumber(outageNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Outage", "outageNumber", outageNumber));
        return mapToResponse(outage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutageResponse> getOutagesWithFilters(Long areaId, String status, String severity, String search, Pageable pageable) {
        Page<Outage> outages = outageRepository.findWithFilters(areaId, status, severity, search, pageable);
        return outages.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public OutageResponse updateOutageStatus(Long outageId, OutageStatusUpdateRequest request) {
        Outage outage = outageRepository.findById(outageId)
                .orElseThrow(() -> new ResourceNotFoundException("Outage", "id", outageId));

        String oldStatus = outage.getStatus();
        String newStatus = request.getStatus().toUpperCase();
        outage.setStatus(newStatus);

        if (request.getRootCause() != null && !request.getRootCause().isBlank()) {
            outage.setRootCause(request.getRootCause());
        }

        if (request.getEstimatedRestoreTime() != null) {
            outage.setEstimatedRestoreTime(request.getEstimatedRestoreTime());
        }

        if ("VERIFIED".equals(newStatus)) {
            outage.setVerifiedAt(LocalDateTime.now());
            notificationService.triggerOutageNotification(outage, "OUTAGE_VERIFIED");
        } else if ("RESTORED".equals(newStatus)) {
            LocalDateTime restoredTime = LocalDateTime.now();
            outage.setActualRestoreTime(restoredTime);
            if (outage.getReportedAt() != null) {
                long durationMinutes = Duration.between(outage.getReportedAt(), restoredTime).toMinutes();
                outage.setRestorationDurationMinutes(durationMinutes);
            }
            notificationService.triggerOutageNotification(outage, "POWER_RESTORED");
        }

        Outage updated = outageRepository.save(outage);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public OutageResponse updateEtr(Long outageId, String etrTimestamp) {
        Outage outage = outageRepository.findById(outageId)
                .orElseThrow(() -> new ResourceNotFoundException("Outage", "id", outageId));

        LocalDateTime newEtr = LocalDateTime.parse(etrTimestamp);
        outage.setEstimatedRestoreTime(newEtr);
        Outage updated = outageRepository.save(outage);

        notificationService.triggerEtrUpdateNotification(updated);
        return mapToResponse(updated);
    }

    public OutageResponse mapToResponse(Outage outage) {
        OutageResponse dto = new OutageResponse();
        dto.setId(outage.getId());
        dto.setOutageNumber(outage.getOutageNumber());
        dto.setTitle(outage.getTitle());
        dto.setDescription(outage.getDescription());
        dto.setAreaId(outage.getArea().getId());
        dto.setAreaName(outage.getArea().getAreaName());
        dto.setAreaCode(outage.getArea().getAreaCode());
        if (outage.getFeeder() != null) {
            dto.setFeederId(outage.getFeeder().getId());
            dto.setFeederName(outage.getFeeder().getFeederName());
            dto.setFeederCode(outage.getFeeder().getFeederCode());
        }
        dto.setSeverity(outage.getSeverity());
        dto.setStatus(outage.getStatus());
        dto.setOutageType(outage.getOutageType());
        dto.setReportedAt(outage.getReportedAt());
        dto.setVerifiedAt(outage.getVerifiedAt());
        dto.setEstimatedRestoreTime(outage.getEstimatedRestoreTime());
        dto.setActualRestoreTime(outage.getActualRestoreTime());
        dto.setRestorationDurationMinutes(outage.getRestorationDurationMinutes());
        dto.setAffectedCustomersCount(outage.getAffectedCustomersCount());
        dto.setRootCause(outage.getRootCause());
        dto.setLatitude(outage.getLatitude());
        dto.setLongitude(outage.getLongitude());
        dto.setReportCount(outage.getReportCount());
        dto.setCreatedByUsername(outage.getCreatedBy() != null ? outage.getCreatedBy().getUsername() : "System");
        return dto;
    }
}
