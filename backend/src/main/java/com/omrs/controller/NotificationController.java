package com.omrs.controller;

import com.omrs.dto.notification.BroadcastNotificationRequest;
import com.omrs.dto.notification.NotificationResponse;
import com.omrs.security.UserPrincipal;
import com.omrs.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Customer Notification & Communication", description = "Endpoints for notification history, area broadcasts, and read status")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/my")
    @Operation(summary = "Get notification history for current logged-in user")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userPrincipal.getId()));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'CUSTOMER')")
    @Operation(summary = "Get notification history for a specific customer")
    public ResponseEntity<List<NotificationResponse>> getCustomerNotifications(@PathVariable Long customerId) {
        return ResponseEntity.ok(notificationService.getNotificationsForCustomer(customerId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Get all system notification audit logs")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @PostMapping("/broadcast")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Send manual broadcast message to all customers in an area")
    public ResponseEntity<Void> sendBroadcast(
            @Valid @RequestBody BroadcastNotificationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        notificationService.sendBroadcast(request, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
