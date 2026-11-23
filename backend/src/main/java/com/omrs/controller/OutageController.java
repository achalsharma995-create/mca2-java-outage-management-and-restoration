package com.omrs.controller;

import com.omrs.dto.outage.OutageReportRequest;
import com.omrs.dto.outage.OutageResponse;
import com.omrs.dto.outage.OutageStatusUpdateRequest;
import com.omrs.security.UserPrincipal;
import com.omrs.service.OutageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/outages")
@Tag(name = "Outage Detection & Reporting", description = "Endpoints for logging outages, filtering, search, and status lifecycle")
public class OutageController {

    @Autowired
    private OutageService outageService;

    @PostMapping
    @Operation(summary = "Report an outage (Customer / Operator)")
    public ResponseEntity<OutageResponse> reportOutage(
            @Valid @RequestBody OutageReportRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        OutageResponse response = outageService.reportOutage(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get outage details by ID")
    public ResponseEntity<OutageResponse> getOutageById(@PathVariable Long id) {
        return ResponseEntity.ok(outageService.getOutageById(id));
    }

    @GetMapping("/number/{outageNumber}")
    @Operation(summary = "Get outage details by incident number")
    public ResponseEntity<OutageResponse> getOutageByNumber(@PathVariable String outageNumber) {
        return ResponseEntity.ok(outageService.getOutageByNumber(outageNumber));
    }

    @GetMapping
    @Operation(summary = "Search and filter outages with pagination")
    public ResponseEntity<Page<OutageResponse>> getOutages(
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reportedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(outageService.getOutagesWithFilters(areaId, status, severity, search, pageable));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'FIELD_ENGINEER')")
    @Operation(summary = "Update outage status (REPORTED -> VERIFIED -> IN_PROGRESS -> RESTORED)")
    public ResponseEntity<OutageResponse> updateOutageStatus(
            @PathVariable Long id,
            @Valid @RequestBody OutageStatusUpdateRequest request) {
        return ResponseEntity.ok(outageService.updateOutageStatus(id, request));
    }

    @PatchMapping("/{id}/etr")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'FIELD_ENGINEER')")
    @Operation(summary = "Update estimated time of restoration (ETR)")
    public ResponseEntity<OutageResponse> updateEtr(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String etr = payload.get("estimatedRestoreTime");
        return ResponseEntity.ok(outageService.updateEtr(id, etr));
    }
}
