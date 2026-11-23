package com.omrs.service;

import com.omrs.dto.workorder.WorkOrderRequest;
import com.omrs.dto.workorder.WorkOrderResponse;
import com.omrs.dto.workorder.WorkOrderStatusUpdateRequest;
import com.omrs.entity.*;
import com.omrs.exception.BadRequestException;
import com.omrs.repository.CrewRepository;
import com.omrs.repository.OutageRepository;
import com.omrs.repository.UserRepository;
import com.omrs.repository.WorkOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkOrderServiceTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private OutageRepository outageRepository;

    @Mock
    private CrewRepository crewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private WorkOrderServiceImpl workOrderService;

    private Outage sampleOutage;
    private Crew availableCrew;
    private Crew busyCrew;
    private User operatorUser;
    private WorkOrder sampleWorkOrder;

    @BeforeEach
    void setUp() {
        Area area = new Area();
        area.setId(1L);
        area.setAreaName("Downtown");

        sampleOutage = new Outage();
        sampleOutage.setId(10L);
        sampleOutage.setOutageNumber("OUT-2026-0001");
        sampleOutage.setTitle("Overhead cable break");
        sampleOutage.setArea(area);
        sampleOutage.setStatus("REPORTED");
        sampleOutage.setReportedAt(LocalDateTime.now().minusHours(1));

        availableCrew = new Crew();
        availableCrew.setId(1L);
        availableCrew.setCrewName("Alpha Crew");
        availableCrew.setAvailabilityStatus("AVAILABLE");
        availableCrew.setBaseArea(area);

        busyCrew = new Crew();
        busyCrew.setId(2L);
        busyCrew.setCrewName("Bravo Crew");
        busyCrew.setAvailabilityStatus("DISPATCHED");
        busyCrew.setBaseArea(area);

        operatorUser = new User("operator1", "op@example.com", "pass", "Sam Carter", "+1-555-0101");
        operatorUser.setId(2L);

        sampleWorkOrder = new WorkOrder();
        sampleWorkOrder.setId(100L);
        sampleWorkOrder.setWoNumber("WO-2026-0001");
        sampleWorkOrder.setOutage(sampleOutage);
        sampleWorkOrder.setCrew(availableCrew);
        sampleWorkOrder.setStatus("WORKING");
        sampleWorkOrder.setCreatedBy(operatorUser);
    }

    @Test
    @DisplayName("Test 1: Create work order and assign available crew marks crew DISPATCHED and outage IN_PROGRESS")
    void testCreateWorkOrder_Success() {
        WorkOrderRequest request = new WorkOrderRequest();
        request.setOutageId(10L);
        request.setCrewId(1L);
        request.setPriority("HIGH");

        when(outageRepository.findById(10L)).thenReturn(Optional.of(sampleOutage));
        when(userRepository.findById(2L)).thenReturn(Optional.of(operatorUser));
        when(crewRepository.findById(1L)).thenReturn(Optional.of(availableCrew));
        when(workOrderRepository.save(any(WorkOrder.class))).thenAnswer(i -> {
            WorkOrder wo = i.getArgument(0);
            wo.setId(101L);
            return wo;
        });

        WorkOrderResponse response = workOrderService.createWorkOrder(request, 2L);

        assertNotNull(response);
        assertEquals("ASSIGNED", response.getStatus());
        assertEquals("DISPATCHED", availableCrew.getAvailabilityStatus());
        assertEquals("IN_PROGRESS", sampleOutage.getStatus());
        verify(crewRepository, times(1)).save(availableCrew);
        verify(outageRepository, times(1)).save(sampleOutage);
        verify(notificationService, times(1)).triggerWorkOrderNotification(any(), eq("CREW_ASSIGNED"));
    }

    @Test
    @DisplayName("Test 2: Assign busy crew throws BadRequestException preventing collision")
    void testAssignCrew_BusyCrewCollision() {
        WorkOrderRequest request = new WorkOrderRequest();
        request.setOutageId(10L);
        request.setCrewId(2L);

        when(outageRepository.findById(10L)).thenReturn(Optional.of(sampleOutage));
        when(userRepository.findById(2L)).thenReturn(Optional.of(operatorUser));
        when(crewRepository.findById(2L)).thenReturn(Optional.of(busyCrew));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> workOrderService.createWorkOrder(request, 2L));

        assertTrue(ex.getMessage().contains("Cannot assign crew"));
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test 3: Update work order to COMPLETED frees crew and auto-closes linked Outage")
    void testUpdateStatus_CompletedAutoClosesOutage() {
        WorkOrderStatusUpdateRequest request = new WorkOrderStatusUpdateRequest();
        request.setStatus("COMPLETED");
        request.setResolutionNotes("Splice wire completed and reclosed");

        when(workOrderRepository.findById(100L)).thenReturn(Optional.of(sampleWorkOrder));
        when(workOrderRepository.save(any(WorkOrder.class))).thenAnswer(i -> i.getArgument(0));

        WorkOrderResponse response = workOrderService.updateWorkOrderStatus(100L, request);

        assertNotNull(response);
        assertEquals("COMPLETED", response.getStatus());
        assertEquals("AVAILABLE", availableCrew.getAvailabilityStatus());
        assertEquals("RESTORED", sampleOutage.getStatus());
        assertNotNull(sampleOutage.getActualRestoreTime());
        verify(crewRepository, times(1)).save(availableCrew);
        verify(outageRepository, times(1)).save(sampleOutage);
        verify(notificationService, times(1)).triggerOutageNotification(sampleOutage, "POWER_RESTORED");
    }
}
