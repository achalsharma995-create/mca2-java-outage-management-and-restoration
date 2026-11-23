package com.omrs.service;

import com.omrs.dto.outage.OutageReportRequest;
import com.omrs.dto.outage.OutageResponse;
import com.omrs.dto.outage.OutageStatusUpdateRequest;
import com.omrs.entity.Area;
import com.omrs.entity.Feeder;
import com.omrs.entity.Outage;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OutageServiceTest {

    @Mock
    private OutageRepository outageRepository;

    @Mock
    private OutageReportRepository outageReportRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private FeederRepository feederRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OutageServiceImpl outageService;

    private Area sampleArea;
    private Feeder sampleFeeder;
    private Outage sampleOutage;

    @BeforeEach
    void setUp() {
        sampleArea = new Area();
        sampleArea.setId(1L);
        sampleArea.setAreaCode("DM-01");
        sampleArea.setAreaName("Downtown Metro");

        sampleFeeder = new Feeder();
        sampleFeeder.setId(10L);
        sampleFeeder.setFeederCode("FDR-101");
        sampleFeeder.setFeederName("Express Feeder");
        sampleFeeder.setArea(sampleArea);

        sampleOutage = new Outage();
        sampleOutage.setId(100L);
        sampleOutage.setOutageNumber("OUT-2026-0001");
        sampleOutage.setTitle("Substation Trip");
        sampleOutage.setDescription("Busbar fault");
        sampleOutage.setArea(sampleArea);
        sampleOutage.setFeeder(sampleFeeder);
        sampleOutage.setSeverity("HIGH");
        sampleOutage.setStatus("REPORTED");
        sampleOutage.setReportedAt(LocalDateTime.now().minusHours(2));
        sampleOutage.setReportCount(1);
        sampleOutage.setAffectedCustomersCount(500);
    }

    @Test
    @DisplayName("Test 1: Report new outage creates unique ticket and notifies control room")
    void testReportOutage_NewIncident() {
        OutageReportRequest request = new OutageReportRequest();
        request.setAreaId(1L);
        request.setFeederId(10L);
        request.setTitle("Downed line");
        request.setDescription("Live wire on street");
        request.setSeverity("CRITICAL");

        when(areaRepository.findById(1L)).thenReturn(Optional.of(sampleArea));
        when(feederRepository.findById(10L)).thenReturn(Optional.of(sampleFeeder));
        when(outageRepository.findActiveOutageByAreaAndFeeder(1L, 10L)).thenReturn(Collections.emptyList());
        when(outageRepository.save(any(Outage.class))).thenAnswer(i -> {
            Outage o = i.getArgument(0);
            o.setId(101L);
            return o;
        });

        OutageResponse response = outageService.reportOutage(request, 1L);

        assertNotNull(response);
        assertEquals("Downed line", response.getTitle());
        assertEquals("CRITICAL", response.getSeverity());
        assertEquals("REPORTED", response.getStatus());
        verify(notificationService, times(1)).triggerOutageNotification(any(), eq("OUTAGE_REPORTED"));
    }

    @Test
    @DisplayName("Test 2: Group duplicate report into existing active incident for same area/feeder")
    void testReportOutage_GroupDuplicateReport() {
        OutageReportRequest request = new OutageReportRequest();
        request.setAreaId(1L);
        request.setFeederId(10L);
        request.setTitle("Another report for same blackout");
        request.setDescription("Lights out on 2nd avenue");

        when(areaRepository.findById(1L)).thenReturn(Optional.of(sampleArea));
        when(feederRepository.findById(10L)).thenReturn(Optional.of(sampleFeeder));
        when(outageRepository.findActiveOutageByAreaAndFeeder(1L, 10L)).thenReturn(Collections.singletonList(sampleOutage));

        OutageResponse response = outageService.reportOutage(request, null);

        assertNotNull(response);
        assertEquals("OUT-2026-0001", response.getOutageNumber());
        assertEquals(2, sampleOutage.getReportCount());
        assertEquals(501, sampleOutage.getAffectedCustomersCount());
        verify(outageReportRepository, times(1)).save(any());
        verify(notificationService, never()).triggerOutageNotification(any(), eq("OUTAGE_REPORTED"));
    }

    @Test
    @DisplayName("Test 3: Update outage status to RESTORED calculates duration and triggers power restored notification")
    void testUpdateOutageStatus_Restored() {
        OutageStatusUpdateRequest request = new OutageStatusUpdateRequest();
        request.setStatus("RESTORED");
        request.setRootCause("Replaced blown fuse");

        when(outageRepository.findById(100L)).thenReturn(Optional.of(sampleOutage));
        when(outageRepository.save(any(Outage.class))).thenAnswer(i -> i.getArgument(0));

        OutageResponse response = outageService.updateOutageStatus(100L, request);

        assertNotNull(response);
        assertEquals("RESTORED", response.getStatus());
        assertNotNull(sampleOutage.getActualRestoreTime());
        assertTrue(sampleOutage.getRestorationDurationMinutes() >= 120);
        verify(notificationService, times(1)).triggerOutageNotification(any(), eq("POWER_RESTORED"));
    }
}
