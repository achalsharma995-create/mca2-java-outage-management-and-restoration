package com.omrs.controller;

import com.omrs.entity.Area;
import com.omrs.entity.Feeder;
import com.omrs.repository.AreaRepository;
import com.omrs.repository.FeederRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/areas")
@Tag(name = "Areas & Distribution Feeders", description = "Endpoints for retrieving geographic distribution zones and substation feeders")
public class AreaLookupController {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private FeederRepository feederRepository;

    @GetMapping
    @Operation(summary = "Get all distribution areas")
    public ResponseEntity<List<Area>> getAllAreas() {
        return ResponseEntity.ok(areaRepository.findAll());
    }

    @GetMapping("/{areaId}/feeders")
    @Operation(summary = "Get all feeders in a specific distribution area")
    public ResponseEntity<List<Feeder>> getFeedersByArea(@PathVariable Long areaId) {
        return ResponseEntity.ok(feederRepository.findByAreaId(areaId));
    }
}
