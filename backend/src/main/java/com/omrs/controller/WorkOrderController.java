package com.omrs.controller;

import com.omrs.dto.workorder.WorkOrderRequest;
import com.omrs.dto.workorder.WorkOrderResponse;
import com.omrs.dto.workorder.WorkOrderStatusUpdateRequest;
import com.omrs.security.UserPrincipal;
import com.omrs.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/work-orders")
@Tag(name = "Crew Dispatch & Work Orders", description = "Endpoints for creating work orders, assigning crews, and status tracking")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Create a work order linked to an outage")
    public ResponseEntity<WorkOrderResponse> createWorkOrder(
            @Valid @RequestBody WorkOrderRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResponseEntity<>(workOrderService.createWorkOrder(request, userPrincipal.getId()), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all work orders")
    public ResponseEntity<List<WorkOrderResponse>> getAllWorkOrders() {
        return ResponseEntity.ok(workOrderService.getAllWorkOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get work order details by ID")
    public ResponseEntity<WorkOrderResponse> getWorkOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(workOrderService.getWorkOrderById(id));
    }

    @GetMapping("/outage/{outageId}")
    @Operation(summary = "Get all work orders for an outage")
    public ResponseEntity<List<WorkOrderResponse>> getWorkOrdersByOutageId(@PathVariable Long outageId) {
        return ResponseEntity.ok(workOrderService.getWorkOrdersByOutageId(outageId));
    }

    @GetMapping("/crew/{crewId}")
    @Operation(summary = "Get all work orders assigned to a crew")
    public ResponseEntity<List<WorkOrderResponse>> getWorkOrdersByCrewId(@PathVariable Long crewId) {
        return ResponseEntity.ok(workOrderService.getWorkOrdersByCrewId(crewId));
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Assign or reassign an available crew to a work order")
    public ResponseEntity<WorkOrderResponse> assignCrew(
            @PathVariable Long id,
            @RequestBody Map<String, Long> payload) {
        Long crewId = payload.get("crewId");
        return ResponseEntity.ok(workOrderService.assignCrewToWorkOrder(id, crewId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'FIELD_ENGINEER', 'CREW_MEMBER')")
    @Operation(summary = "Update work order status (CREATED -> ASSIGNED -> EN_ROUTE -> WORKING -> COMPLETED)")
    public ResponseEntity<WorkOrderResponse> updateWorkOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody WorkOrderStatusUpdateRequest request) {
        return ResponseEntity.ok(workOrderService.updateWorkOrderStatus(id, request));
    }
}
