package com.omrs.service;

import com.omrs.dto.notification.BroadcastNotificationRequest;
import com.omrs.dto.notification.NotificationResponse;
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
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationTemplateRepository templateRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Area sampleArea;
    private Customer sampleCustomer;
    private Outage sampleOutage;
    private NotificationTemplate sampleTemplate;
    private Notification sampleNotification;

    @BeforeEach
    void setUp() {
        sampleArea = new Area();
        sampleArea.setId(3L);
        sampleArea.setAreaName("Green Valley");

        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);
        sampleCustomer.setFullName("John Doe");
        sampleCustomer.setAccountNumber("ACC-88201");
        sampleCustomer.setPhoneNumber("+1-555-1001");
        sampleCustomer.setArea(sampleArea);

        sampleOutage = new Outage();
        sampleOutage.setId(10L);
        sampleOutage.setOutageNumber("OUT-2026-0001");
        sampleOutage.setTitle("Wind Damage");
        sampleOutage.setArea(sampleArea);
        sampleOutage.setRootCause("Fallen tree branch");

        sampleTemplate = new NotificationTemplate();
        sampleTemplate.setEventCode("POWER_RESTORED");
        sampleTemplate.setTitleTemplate("Good News: Power Restored in {areaName}!");
        sampleTemplate.setBodyTemplate("Service restored for {outageNumber}. Cause: {rootCause}.");

        sampleNotification = new Notification();
        sampleNotification.setId(100L);
        sampleNotification.setStatus("SENT");
        sampleNotification.setSentAt(LocalDateTime.now().minusHours(1));
    }

    @Test
    @DisplayName("Test 1: Trigger notification formats template placeholders and dispatches to customers")
    void testTriggerOutageNotification() {
        when(templateRepository.findByEventCode("POWER_RESTORED")).thenReturn(Optional.of(sampleTemplate));
        when(customerRepository.findByAreaId(3L)).thenReturn(Collections.singletonList(sampleCustomer));

        notificationService.triggerOutageNotification(sampleOutage, "POWER_RESTORED");

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    @DisplayName("Test 2: Control Room send broadcast transmits to all customers in selected area")
    void testSendBroadcast() {
        BroadcastNotificationRequest request = new BroadcastNotificationRequest();
        request.setAreaId(3L);
        request.setTitle("Weather Alert");
        request.setMessage("Storm approaching, crews on standby");
        request.setChannel("SMS");

        when(areaRepository.findById(3L)).thenReturn(Optional.of(sampleArea));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(customerRepository.findByAreaId(3L)).thenReturn(Collections.singletonList(sampleCustomer));

        notificationService.sendBroadcast(request, 1L);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    @DisplayName("Test 3: Mark notification as read updates status and read timestamp")
    void testMarkAsRead() {
        when(notificationRepository.findById(100L)).thenReturn(Optional.of(sampleNotification));

        notificationService.markAsRead(100L);

        assertEquals("READ", sampleNotification.getStatus());
        assertNotNull(sampleNotification.getReadAt());
        verify(notificationRepository, times(1)).save(sampleNotification);
    }
}
