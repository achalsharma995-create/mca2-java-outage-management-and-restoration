package com.omrs.service;

import com.omrs.dto.progress.ProgressUpdateRequest;
import com.omrs.dto.progress.ProgressUpdateResponse;
import com.omrs.entity.Outage;
import com.omrs.entity.ProgressUpdate;
import com.omrs.entity.User;
import com.omrs.entity.WorkOrder;
import com.omrs.exception.ResourceNotFoundException;
import com.omrs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestorationProgressServiceImpl implements RestorationProgressService {

    @Autowired
    private ProgressUpdateRepository progressUpdateRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private OutageRepository outageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public ProgressUpdateResponse addProgressUpdate(ProgressUpdateRequest request, Long engineerUserId) {
        WorkOrder workOrder = workOrderRepository.findById(request.getWorkOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder", "id", request.getWorkOrderId()));

        User engineer = userRepository.findById(engineerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", engineerUserId));

        Outage outage = workOrder.getOutage();

        ProgressUpdate update = new ProgressUpdate();
        update.setWorkOrder(workOrder);
        update.setOutage(outage);
        update.setEngineer(engineer);
        update.setPercentageComplete(request.getPercentageComplete());
        update.setStatusReached(request.getStatusReached());
        update.setRemarks(request.getRemarks());
        update.setEstimatedRestorationTime(request.getEstimatedRestorationTime());
        update.setCreatedAt(LocalDateTime.now());

        ProgressUpdate saved = progressUpdateRepository.save(update);

        // Update outage ETR if provided
        if (request.getEstimatedRestorationTime() != null) {
            outage.setEstimatedRestoreTime(request.getEstimatedRestorationTime());
            outageRepository.save(outage);
            notificationService.triggerEtrUpdateNotification(outage);
        }

        // If progress reaches 100%, trigger auto-completion of work order and outage
        if (request.getPercentageComplete() >= 100) {
            LocalDateTime now = LocalDateTime.now();
            workOrder.setStatus("COMPLETED");
            workOrder.setCompletedAt(now);
            workOrder.setResolutionNotes(request.getRemarks());
            workOrderRepository.save(workOrder);

            if (workOrder.getCrew() != null) {
                workOrder.getCrew().setAvailabilityStatus("AVAILABLE");
                crewRepository.save(workOrder.getCrew());
            }

            outage.setStatus("RESTORED");
            outage.setActualRestoreTime(now);
            if (outage.getReportedAt() != null) {
                long durationMinutes = Duration.between(outage.getReportedAt(), now).toMinutes();
                outage.setRestorationDurationMinutes(durationMinutes);
            }
            outageRepository.save(outage);

            notificationService.triggerOutageNotification(outage, "POWER_RESTORED");
        }

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressUpdateResponse> getTimelineForOutage(Long outageId) {
        return progressUpdateRepository.findByOutageIdOrderByCreatedAtAsc(outageId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressUpdateResponse> getProgressForWorkOrder(Long workOrderId) {
        return progressUpdateRepository.findByWorkOrderIdOrderByCreatedAtAsc(workOrderId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProgressUpdateResponse mapToResponse(ProgressUpdate update) {
        ProgressUpdateResponse dto = new ProgressUpdateResponse();
        dto.setId(update.getId());
        dto.setWorkOrderId(update.getWorkOrder().getId());
        dto.setWoNumber(update.getWorkOrder().getWoNumber());
        dto.setOutageId(update.getOutage().getId());
        dto.setOutageNumber(update.getOutage().getOutageNumber());
        dto.setEngineerId(update.getEngineer().getId());
        dto.setEngineerName(update.getEngineer().getFullName());
        dto.setPercentageComplete(update.getPercentageComplete());
        dto.setStatusReached(update.getStatusReached());
        dto.setRemarks(update.getRemarks());
        dto.setEstimatedRestorationTime(update.getEstimatedRestorationTime());
        dto.setCreatedAt(update.getCreatedAt());
        return dto;
    }
}
