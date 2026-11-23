package com.omrs.service;

import com.omrs.dto.crew.CrewRequest;
import com.omrs.dto.crew.CrewResponse;

import java.util.List;

public interface CrewService {
    CrewResponse createCrew(CrewRequest request);
    CrewResponse updateCrew(Long crewId, CrewRequest request);
    CrewResponse getCrewById(Long crewId);
    List<CrewResponse> getAllCrews();
    List<CrewResponse> getAvailableCrews();
    void deleteCrew(Long crewId);
    void updateCrewStatus(Long crewId, String availabilityStatus);
}
