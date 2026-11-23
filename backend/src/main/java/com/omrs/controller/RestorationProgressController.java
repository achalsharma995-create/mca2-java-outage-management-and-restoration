package com.omrs.controller;

import com.omrs.dto.progress.ProgressUpdateRequest;
import com.omrs.dto.progress.ProgressUpdateResponse;
import com.omrs.security.UserPrincipal;
import com.omrs.service.RestorationProgressService;
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

@RestController
@RequestMapping("/progress")
@Tag(name = "Restoration Progress Tracking", description = "Endpoints for logging field restoration milestones and timeline view")
public class RestorationProgressController {

    @Autowired
    private RestorationProgressService progressService;

    @PostMapping
    @PreAuthorize("hasAnyRole('FIELD_ENGINEER', 'CREW_MEMBER', 'ADMIN', 'OPERATOR')")
    @Operation(summary = "Post field restoration progress update (percentage, remarks, ETR)")
    public ResponseEntity<ProgressUpdateResponse> addProgressUpdate(
            @Valid @RequestBody ProgressUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResponseEntity<>(progressService.addProgressUpdate(request, userPrincipal.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/outage/{outageId}/timeline")
    @Operation(summary = "Get chronological restoration progress timeline for an outage")
    public ResponseEntity<List<ProgressUpdateResponse>> getTimelineForOutage(@PathVariable Long outageId) {
        return ResponseEntity.ok(progressService.getTimelineForOutage(outageId));
    }

    @GetMapping("/work-order/{workOrderId}")
    @Operation(summary = "Get progress milestones for a specific work order")
    public ResponseEntity<List<ProgressUpdateResponse>> getProgressForWorkOrder(@PathVariable Long workOrderId) {
        return ResponseEntity.ok(progressService.getProgressForWorkOrder(workOrderId));
    }
}
