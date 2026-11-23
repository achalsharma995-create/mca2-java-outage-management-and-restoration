package com.omrs.service;

import com.omrs.dto.notification.BroadcastNotificationRequest;
import com.omrs.dto.notification.NotificationResponse;
import com.omrs.entity.*;
import com.omrs.exception.ResourceNotFoundException;
import com.omrs.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTemplateRepository templateRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void triggerOutageNotification(Outage outage, String eventCode) {
        NotificationTemplate template = templateRepository.findByEventCode(eventCode)
                .orElse(null);

        String title = "Grid Alert: " + outage.getTitle();
        String body = "Outage update for reference: " + outage.getOutageNumber();

        if (template != null) {
            title = template.getTitleTemplate()
                    .replace("{areaName}", outage.getArea().getAreaName())
                    .replace("{outageNumber}", outage.getOutageNumber());

            body = template.getBodyTemplate()
                    .replace("{areaName}", outage.getArea().getAreaName())
                    .replace("{outageNumber}", outage.getOutageNumber())
                    .replace("{rootCause}", outage.getRootCause() != null ? outage.getRootCause() : "Under Investigation");
        }

        dispatchToAffectedCustomers(outage.getArea().getId(), outage, eventCode, title, body);
    }

    @Override
    @Transactional
    public void triggerWorkOrderNotification(WorkOrder workOrder, String eventCode) {
        NotificationTemplate template = templateRepository.findByEventCode(eventCode)
                .orElse(null);

        Outage outage = workOrder.getOutage();
        String crewName = workOrder.getCrew() != null ? workOrder.getCrew().getCrewName() : "Assigned Crew";

        String title = "Crew Dispatch Alert: " + outage.getOutageNumber();
        String body = "Crew " + crewName + " dispatched to " + outage.getOutageNumber();

        if (template != null) {
            title = template.getTitleTemplate()
                    .replace("{outageNumber}", outage.getOutageNumber())
                    .replace("{crewName}", crewName)
                    .replace("{woNumber}", workOrder.getWoNumber());

            body = template.getBodyTemplate()
                    .replace("{outageNumber}", outage.getOutageNumber())
                    .replace("{crewName}", crewName)
                    .replace("{woNumber}", workOrder.getWoNumber());
        }

        dispatchToAffectedCustomers(outage.getArea().getId(), outage, eventCode, title, body);
    }

    @Override
    @Transactional
    public void triggerEtrUpdateNotification(Outage outage) {
        NotificationTemplate template = templateRepository.findByEventCode("ETR_UPDATED")
                .orElse(null);

        String etrStr = outage.getEstimatedRestoreTime() != null ?
                outage.getEstimatedRestoreTime().format(DATE_FORMATTER) : "Pending Assessment";

        String title = "ETR Update for Outage " + outage.getOutageNumber();
        String body = "Estimated restoration time updated to " + etrStr;

        if (template != null) {
            title = template.getTitleTemplate()
                    .replace("{outageNumber}", outage.getOutageNumber())
                    .replace("{etr}", etrStr);

            body = template.getBodyTemplate()
                    .replace("{outageNumber}", outage.getOutageNumber())
                    .replace("{etr}", etrStr);
        }

        dispatchToAffectedCustomers(outage.getArea().getId(), outage, "ETR_UPDATED", title, body);
    }

    private void dispatchToAffectedCustomers(Long areaId, Outage outage, String eventCode, String title, String body) {
        List<Customer> customers = customerRepository.findByAreaId(areaId);

        for (Customer customer : customers) {
            Notification notification = new Notification();
            notification.setCustomer(customer);
            notification.setUser(customer.getUser());
            notification.setOutage(outage);
            notification.setArea(outage.getArea());
            notification.setEventType(eventCode);
            notification.setTitle(title);
            notification.setMessage(body);
            notification.setChannel("SMS,EMAIL,IN_APP");
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());

            notificationRepository.save(notification);

            // Simulate SMS / Email transmission via application logs
            logger.info("[GATEWAY DISPATCH SIMULATION] Sent {} to Customer {} ({}) | Phone: {} | Email: {} | Subject: {}",
                    eventCode, customer.getFullName(), customer.getAccountNumber(),
                    customer.getPhoneNumber(), customer.getEmail(), title);
        }
    }

    @Override
    @Transactional
    public void sendBroadcast(BroadcastNotificationRequest request, Long senderUserId) {
        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Area", "id", request.getAreaId()));

        User sender = userRepository.findById(senderUserId)
                .orElse(null);

        List<Customer> customers = customerRepository.findByAreaId(area.getId());

        for (Customer customer : customers) {
            Notification notification = new Notification();
            notification.setCustomer(customer);
            notification.setUser(customer.getUser());
            notification.setArea(area);
            notification.setEventType("AREA_BROADCAST");
            notification.setTitle(request.getTitle());
            notification.setMessage(request.getMessage());
            notification.setChannel(request.getChannel());
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());

            notificationRepository.save(notification);

            logger.info("[BROADCAST SIMULATION] Broadcast to {} | Area: {} | Message: {}",
                    customer.getFullName(), area.getAreaName(), request.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsForCustomer(Long customerId) {
        return notificationRepository.findByCustomerIdOrderBySentAtDesc(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdOrderBySentAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAllByOrderBySentAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setReadAt(LocalDateTime.now());
        notification.setStatus("READ");
        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse dto = new NotificationResponse();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUser() != null ? notification.getUser().getId() : null);
        dto.setCustomerId(notification.getCustomer() != null ? notification.getCustomer().getId() : null);
        dto.setCustomerName(notification.getCustomer() != null ? notification.getCustomer().getFullName() : "Broadcast / System");
        dto.setOutageId(notification.getOutage() != null ? notification.getOutage().getId() : null);
        dto.setOutageNumber(notification.getOutage() != null ? notification.getOutage().getOutageNumber() : null);
        dto.setAreaId(notification.getArea() != null ? notification.getArea().getId() : null);
        dto.setAreaName(notification.getArea() != null ? notification.getArea().getAreaName() : null);
        dto.setEventType(notification.getEventType());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setChannel(notification.getChannel());
        dto.setStatus(notification.getStatus());
        dto.setSentAt(notification.getSentAt());
        dto.setReadAt(notification.getReadAt());
        return dto;
    }
}
