package com.omrs.controller;

import com.omrs.dto.crew.CrewRequest;
import com.omrs.dto.crew.CrewResponse;
import com.omrs.service.CrewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crews")
@Tag(name = "Crew Management", description = "Endpoints for restoration crews, fleet status, and availability tracking")
public class CrewController {

    @Autowired
    private CrewService crewService;

    @GetMapping
    @Operation(summary = "Get all restoration crews")
    public ResponseEntity<List<CrewResponse>> getAllCrews() {
        return ResponseEntity.ok(crewService.getAllCrews());
    }

    @GetMapping("/available")
    @Operation(summary = "Get only crews currently marked AVAILABLE for dispatch")
    public ResponseEntity<List<CrewResponse>> getAvailableCrews() {
        return ResponseEntity.ok(crewService.getAvailableCrews());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get crew details by ID")
    public ResponseEntity<CrewResponse> getCrewById(@PathVariable Long id) {
        return ResponseEntity.ok(crewService.getCrewById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Create a new restoration crew")
    public ResponseEntity<CrewResponse> createCrew(@Valid @RequestBody CrewRequest request) {
        return new ResponseEntity<>(crewService.createCrew(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Update an existing restoration crew")
    public ResponseEntity<CrewResponse> updateCrew(@PathVariable Long id, @Valid @RequestBody CrewRequest request) {
        return ResponseEntity.ok(crewService.updateCrew(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a crew")
    public ResponseEntity<Void> deleteCrew(@PathVariable Long id) {
        crewService.deleteCrew(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'FIELD_ENGINEER')")
    @Operation(summary = "Update crew availability status (AVAILABLE, DISPATCHED, ON_DUTY, etc.)")
    public ResponseEntity<Void> updateCrewStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        crewService.updateCrewStatus(id, payload.get("availabilityStatus"));
        return ResponseEntity.ok().build();
    }
}
