package com.omrs.service;

import com.omrs.dto.progress.ProgressUpdateRequest;
import com.omrs.dto.progress.ProgressUpdateResponse;
import com.omrs.entity.*;
import com.omrs.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestorationProgressServiceTest {

    @Mock
    private ProgressUpdateRepository progressUpdateRepository;

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private OutageRepository outageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CrewRepository crewRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RestorationProgressServiceImpl progressService;

    private Outage sampleOutage;
    private WorkOrder sampleWorkOrder;
    private User engineerUser;
    private Crew sampleCrew;

    @BeforeEach
    void setUp() {
        Area area = new Area();
        area.setId(1L);
        area.setAreaName("Metro");

        sampleOutage = new Outage();
        sampleOutage.setId(10L);
        sampleOutage.setOutageNumber("OUT-2026-0001");
        sampleOutage.setTitle("Blown transformer");
        sampleOutage.setArea(area);
        sampleOutage.setStatus("IN_PROGRESS");
        sampleOutage.setReportedAt(LocalDateTime.now().minusHours(2));

        sampleCrew = new Crew();
        sampleCrew.setId(2L);
        sampleCrew.setCrewName("Bravo Crew");
        sampleCrew.setAvailabilityStatus("ON_DUTY");

        sampleWorkOrder = new WorkOrder();
        sampleWorkOrder.setId(100L);
        sampleWorkOrder.setWoNumber("WO-2026-0001");
        sampleWorkOrder.setOutage(sampleOutage);
        sampleWorkOrder.setCrew(sampleCrew);
        sampleWorkOrder.setStatus("WORKING");

        engineerUser = new User("engineer1", "eng@example.com", "pass", "Marcus Vance", "+1-555-0201");
        engineerUser.setId(4L);
    }

    @Test
    @DisplayName("Test 1: Add intermediate milestone (40%) saves progress and updates ETR")
    void testAddProgressUpdate_Intermediate() {
        ProgressUpdateRequest request = new ProgressUpdateRequest();
        request.setWorkOrderId(100L);
        request.setPercentageComplete(40);
        request.setStatusReached("VEGETATION_CLEARED");
        request.setRemarks("Tree cut away from lines");
        LocalDateTime newEtr = LocalDateTime.now().plusHours(2);
        request.setEstimatedRestorationTime(newEtr);

        when(workOrderRepository.findById(100L)).thenReturn(Optional.of(sampleWorkOrder));
        when(userRepository.findById(4L)).thenReturn(Optional.of(engineerUser));
        when(progressUpdateRepository.save(any(ProgressUpdate.class))).thenAnswer(i -> {
            ProgressUpdate pu = i.getArgument(0);
            pu.setId(1L);
            return pu;
        });

        ProgressUpdateResponse response = progressService.addProgressUpdate(request, 4L);

        assertNotNull(response);
        assertEquals(40, response.getPercentageComplete());
        assertEquals("VEGETATION_CLEARED", response.getStatusReached());
        assertEquals(newEtr, sampleOutage.getEstimatedRestoreTime());
        verify(outageRepository, times(1)).save(sampleOutage);
        verify(notificationService, times(1)).triggerEtrUpdateNotification(sampleOutage);
    }

    @Test
    @DisplayName("Test 2: Reaching 100% progress auto-completes work order, frees crew, and closes Outage to RESTORED")
    void testAddProgressUpdate_100PercentAutoCloses() {
        ProgressUpdateRequest request = new ProgressUpdateRequest();
        request.setWorkOrderId(100L);
        request.setPercentageComplete(100);
        request.setStatusReached("ENERGIZED_AND_RESTORED");
        request.setRemarks("Feeder closed, voltage nominal on all phases");

        when(workOrderRepository.findById(100L)).thenReturn(Optional.of(sampleWorkOrder));
        when(userRepository.findById(4L)).thenReturn(Optional.of(engineerUser));
        when(progressUpdateRepository.save(any(ProgressUpdate.class))).thenAnswer(i -> {
            ProgressUpdate pu = i.getArgument(0);
            pu.setId(2L);
            return pu;
        });

        ProgressUpdateResponse response = progressService.addProgressUpdate(request, 4L);

        assertNotNull(response);
        assertEquals("COMPLETED", sampleWorkOrder.getStatus());
        assertNotNull(sampleWorkOrder.getCompletedAt());
        assertEquals("AVAILABLE", sampleCrew.getAvailabilityStatus());
        assertEquals("RESTORED", sampleOutage.getStatus());
        assertNotNull(sampleOutage.getActualRestoreTime());
        assertTrue(sampleOutage.getRestorationDurationMinutes() >= 120);
        verify(crewRepository, times(1)).save(sampleCrew);
        verify(outageRepository, times(1)).save(sampleOutage);
        verify(notificationService, times(1)).triggerOutageNotification(sampleOutage, "POWER_RESTORED");
    }

    @Test
    @DisplayName("Test 3: Get chronological timeline returns ordered progress milestones")
    void testGetTimelineForOutage() {
        ProgressUpdate pu = new ProgressUpdate();
        pu.setId(1L);
        pu.setWorkOrder(sampleWorkOrder);
        pu.setOutage(sampleOutage);
        pu.setEngineer(engineerUser);
        pu.setPercentageComplete(50);
        pu.setStatusReached("FAULT_ISOLATED");
        pu.setRemarks("Fault identified and isolated");
        pu.setCreatedAt(LocalDateTime.now());

        when(progressUpdateRepository.findByOutageIdOrderByCreatedAtAsc(10L))
                .thenReturn(Collections.singletonList(pu));

        List<ProgressUpdateResponse> timeline = progressService.getTimelineForOutage(10L);

        assertNotNull(timeline);
        assertEquals(1, timeline.size());
        assertEquals(50, timeline.get(0).getPercentageComplete());
        assertEquals("Marcus Vance", timeline.get(0).getEngineerName());
    }
}
