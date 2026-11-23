package com.omrs.service;

import com.omrs.dto.notification.BroadcastNotificationRequest;
import com.omrs.dto.notification.NotificationResponse;
import com.omrs.entity.Outage;
import com.omrs.entity.WorkOrder;

import java.util.List;

public interface NotificationService {
    void triggerOutageNotification(Outage outage, String eventCode);
    void triggerWorkOrderNotification(WorkOrder workOrder, String eventCode);
    void triggerEtrUpdateNotification(Outage outage);
    void sendBroadcast(BroadcastNotificationRequest request, Long senderUserId);
    List<NotificationResponse> getNotificationsForCustomer(Long customerId);
    List<NotificationResponse> getNotificationsForUser(Long userId);
    List<NotificationResponse> getAllNotifications();
    void markAsRead(Long notificationId);
}
