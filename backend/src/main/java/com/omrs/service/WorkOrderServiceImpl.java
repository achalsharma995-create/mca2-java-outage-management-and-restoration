package com.omrs.service;

import com.omrs.dto.workorder.WorkOrderRequest;
import com.omrs.dto.workorder.WorkOrderResponse;
import com.omrs.dto.workorder.WorkOrderStatusUpdateRequest;
import com.omrs.entity.Crew;
import com.omrs.entity.Outage;
import com.omrs.entity.User;
import com.omrs.entity.WorkOrder;
import com.omrs.exception.BadRequestException;
import com.omrs.exception.ResourceNotFoundException;
import com.omrs.repository.CrewRepository;
import com.omrs.repository.OutageRepository;
import com.omrs.repository.UserRepository;
import com.omrs.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private OutageRepository outageRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public WorkOrderResponse createWorkOrder(WorkOrderRequest request, Long creatorUserId) {
        Outage outage = outageRepository.findById(request.getOutageId())
                .orElseThrow(() -> new ResourceNotFoundException("Outage", "id", request.getOutageId()));

        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", creatorUserId));

        WorkOrder workOrder = new WorkOrder();
        String generatedWoNumber = "WO-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        workOrder.setWoNumber(generatedWoNumber);
        workOrder.setOutage(outage);
        workOrder.setPriority(request.getPriority() != null ? request.getPriority() : "MEDIUM");
        workOrder.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes() != null ? request.getEstimatedDurationMinutes() : 120);
        workOrder.setInstructions(request.getInstructions());
        workOrder.setCreatedBy(creator);
        workOrder.setStatus("CREATED");

        if (request.getCrewId() != null) {
            Crew crew = crewRepository.findById(request.getCrewId())
                    .orElseThrow(() -> new ResourceNotFoundException("Crew", "id", request.getCrewId()));

            // Prevent assigning a crew that is already busy
            if (!"AVAILABLE".equalsIgnoreCase(crew.getAvailabilityStatus())) {
                throw new BadRequestException("Cannot assign crew '" + crew.getCrewName() + "'. Current status: " + crew.getAvailabilityStatus());
            }

            workOrder.setCrew(crew);
            workOrder.setStatus("ASSIGNED");
            workOrder.setAssignedAt(LocalDateTime.now());

            crew.setAvailabilityStatus("DISPATCHED");
            crewRepository.save(crew);

            // Outage transitions to IN_PROGRESS
            outage.setStatus("IN_PROGRESS");
            outageRepository.save(outage);
        }

        WorkOrder savedWo = workOrderRepository.save(workOrder);

        if (savedWo.getCrew() != null) {
            notificationService.triggerWorkOrderNotification(savedWo, "CREW_ASSIGNED");
        }

        return mapToResponse(savedWo);
    }

    @Override
    @Transactional
    public WorkOrderResponse assignCrewToWorkOrder(Long workOrderId, Long crewId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder", "id", workOrderId));

        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ResourceNotFoundException("Crew", "id", crewId));

        // Prevent assigning busy crew
        if (!"AVAILABLE".equalsIgnoreCase(crew.getAvailabilityStatus())) {
            throw new BadRequestException("Cannot assign crew '" + crew.getCrewName() + "'. Crew is currently: " + crew.getAvailabilityStatus());
        }

        // Release old crew if any
        if (workOrder.getCrew() != null && !workOrder.getCrew().getId().equals(crewId)) {
            Crew oldCrew = workOrder.getCrew();
            oldCrew.setAvailabilityStatus("AVAILABLE");
            crewRepository.save(oldCrew);
        }

        workOrder.setCrew(crew);
        workOrder.setStatus("ASSIGNED");
        workOrder.setAssignedAt(LocalDateTime.now());

        crew.setAvailabilityStatus("DISPATCHED");
        crewRepository.save(crew);

        Outage outage = workOrder.getOutage();
        if (!"RESTORED".equals(outage.getStatus())) {
            outage.setStatus("IN_PROGRESS");
            outageRepository.save(outage);
        }

        WorkOrder updated = workOrderRepository.save(workOrder);
        notificationService.triggerWorkOrderNotification(updated, "CREW_ASSIGNED");

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public WorkOrderResponse updateWorkOrderStatus(Long workOrderId, WorkOrderStatusUpdateRequest request) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder", "id", workOrderId));

        String newStatus = request.getStatus().toUpperCase();
        workOrder.setStatus(newStatus);

        if (request.getResolutionNotes() != null) {
            workOrder.setResolutionNotes(request.getResolutionNotes());
        }

        LocalDateTime now = LocalDateTime.now();

        if ("EN_ROUTE".equals(newStatus)) {
            workOrder.setDispatchedAt(now);
            if (workOrder.getCrew() != null) {
                workOrder.getCrew().setAvailabilityStatus("DISPATCHED");
                crewRepository.save(workOrder.getCrew());
            }
            notificationService.triggerWorkOrderNotification(workOrder, "CREW_EN_ROUTE");
        } else if ("WORKING".equals(newStatus)) {
            workOrder.setArrivedAt(now);
            if (workOrder.getCrew() != null) {
                workOrder.getCrew().setAvailabilityStatus("ON_DUTY");
                crewRepository.save(workOrder.getCrew());
            }
        } else if ("COMPLETED".equals(newStatus)) {
            workOrder.setCompletedAt(now);

            // Free the crew
            if (workOrder.getCrew() != null) {
                workOrder.getCrew().setAvailabilityStatus("AVAILABLE");
                crewRepository.save(workOrder.getCrew());
            }

            // Auto-close the outage and calculate restoration duration
            Outage outage = workOrder.getOutage();
            outage.setStatus("RESTORED");
            outage.setActualRestoreTime(now);
            if (outage.getReportedAt() != null) {
                long durationMinutes = Duration.between(outage.getReportedAt(), now).toMinutes();
                outage.setRestorationDurationMinutes(durationMinutes);
            }
            if (request.getResolutionNotes() != null) {
                outage.setRootCause(request.getResolutionNotes());
            }
            outageRepository.save(outage);

            notificationService.triggerOutageNotification(outage, "POWER_RESTORED");
        } else if ("CANCELLED".equals(newStatus)) {
            if (workOrder.getCrew() != null) {
                workOrder.getCrew().setAvailabilityStatus("AVAILABLE");
                crewRepository.save(workOrder.getCrew());
            }
        }

        WorkOrder updated = workOrderRepository.save(workOrder);
        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderResponse getWorkOrderById(Long workOrderId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder", "id", workOrderId));
        return mapToResponse(workOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> getAllWorkOrders() {
        return workOrderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> getWorkOrdersByOutageId(Long outageId) {
        return workOrderRepository.findByOutageId(outageId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> getWorkOrdersByCrewId(Long crewId) {
        return workOrderRepository.findByCrewId(crewId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public WorkOrderResponse mapToResponse(WorkOrder wo) {
        WorkOrderResponse dto = new WorkOrderResponse();
        dto.setId(wo.getId());
        dto.setWoNumber(wo.getWoNumber());
        dto.setOutageId(wo.getOutage().getId());
        dto.setOutageNumber(wo.getOutage().getOutageNumber());
        dto.setOutageTitle(wo.getOutage().getTitle());
        dto.setOutageSeverity(wo.getOutage().getSeverity());
        dto.setOutageAreaName(wo.getOutage().getArea().getAreaName());

        if (wo.getCrew() != null) {
            dto.setCrewId(wo.getCrew().getId());
            dto.setCrewCode(wo.getCrew().getCrewCode());
            dto.setCrewName(wo.getCrew().getCrewName());
            dto.setCrewVehicleNumber(wo.getCrew().getVehicleNumber());
        }

        dto.setPriority(wo.getPriority());
        dto.setStatus(wo.getStatus());
        dto.setAssignedAt(wo.getAssignedAt());
        dto.setDispatchedAt(wo.getDispatchedAt());
        dto.setArrivedAt(wo.getArrivedAt());
        dto.setCompletedAt(wo.getCompletedAt());
        dto.setEstimatedDurationMinutes(wo.getEstimatedDurationMinutes());
        dto.setInstructions(wo.getInstructions());
        dto.setResolutionNotes(wo.getResolutionNotes());
        dto.setCreatedByUsername(wo.getCreatedBy().getUsername());
        dto.setCreatedAt(wo.getCreatedAt());

        return dto;
    }
}
