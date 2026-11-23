package com.omrs.service;

import com.omrs.dto.outage.OutageReportRequest;
import com.omrs.dto.outage.OutageResponse;
import com.omrs.dto.outage.OutageStatusUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutageService {
    OutageResponse reportOutage(OutageReportRequest request, Long reporterUserId);
    OutageResponse getOutageById(Long outageId);
    OutageResponse getOutageByNumber(String outageNumber);
    Page<OutageResponse> getOutagesWithFilters(Long areaId, String status, String severity, String search, Pageable pageable);
    OutageResponse updateOutageStatus(Long outageId, OutageStatusUpdateRequest request);
    OutageResponse updateEtr(Long outageId, String etrTimestamp);
}
