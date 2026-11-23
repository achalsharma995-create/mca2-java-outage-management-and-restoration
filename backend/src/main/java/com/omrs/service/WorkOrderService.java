package com.omrs.service;

import com.omrs.dto.workorder.WorkOrderRequest;
import com.omrs.dto.workorder.WorkOrderResponse;
import com.omrs.dto.workorder.WorkOrderStatusUpdateRequest;

import java.util.List;

public interface WorkOrderService {
    WorkOrderResponse createWorkOrder(WorkOrderRequest request, Long creatorUserId);
    WorkOrderResponse assignCrewToWorkOrder(Long workOrderId, Long crewId);
    WorkOrderResponse updateWorkOrderStatus(Long workOrderId, WorkOrderStatusUpdateRequest request);
    WorkOrderResponse getWorkOrderById(Long workOrderId);
    List<WorkOrderResponse> getAllWorkOrders();
    List<WorkOrderResponse> getWorkOrdersByOutageId(Long outageId);
    List<WorkOrderResponse> getWorkOrdersByCrewId(Long crewId);
}
