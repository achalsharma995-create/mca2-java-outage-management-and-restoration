package com.omrs.service;

import com.omrs.dto.crew.CrewRequest;
import com.omrs.dto.crew.CrewResponse;
import com.omrs.entity.Area;
import com.omrs.entity.Crew;
import com.omrs.exception.BadRequestException;
import com.omrs.repository.AreaRepository;
import com.omrs.repository.CrewMemberRepository;
import com.omrs.repository.CrewRepository;
import com.omrs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrewServiceTest {

    @Mock
    private CrewRepository crewRepository;

    @Mock
    private CrewMemberRepository crewMemberRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CrewServiceImpl crewService;

    private Area sampleArea;
    private Crew sampleCrew;

    @BeforeEach
    void setUp() {
        sampleArea = new Area();
        sampleArea.setId(1L);
        sampleArea.setAreaCode("DM-01");
        sampleArea.setAreaName("Downtown Metro");

        sampleCrew = new Crew();
        sampleCrew.setId(5L);
        sampleCrew.setCrewCode("CRW-ALPHA");
        sampleCrew.setCrewName("Alpha Rapid Response");
        sampleCrew.setVehicleNumber("TRK-101");
        sampleCrew.setAvailabilityStatus("AVAILABLE");
        sampleCrew.setBaseArea(sampleArea);
        sampleCrew.setMembersCount(4);
    }

    @Test
    @DisplayName("Test 1: Create crew successfully with base area")
    void testCreateCrew_Success() {
        CrewRequest request = new CrewRequest();
        request.setCrewCode("CRW-BRAVO");
        request.setCrewName("Bravo Line Crew");
        request.setVehicleNumber("TRK-102");
        request.setContactPhone("+1-555-0102");
        request.setBaseAreaId(1L);

        when(crewRepository.findByCrewCode("CRW-BRAVO")).thenReturn(Optional.empty());
        when(areaRepository.findById(1L)).thenReturn(Optional.of(sampleArea));
        when(crewRepository.save(any(Crew.class))).thenAnswer(i -> {
            Crew c = i.getArgument(0);
            c.setId(6L);
            return c;
        });

        CrewResponse response = crewService.createCrew(request);

        assertNotNull(response);
        assertEquals("CRW-BRAVO", response.getCrewCode());
        assertEquals("AVAILABLE", response.getAvailabilityStatus());
        verify(crewRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test 2: Prevent creating crew with duplicate crew code")
    void testCreateCrew_DuplicateCode() {
        CrewRequest request = new CrewRequest();
        request.setCrewCode("CRW-ALPHA");
        request.setCrewName("Duplicate Alpha");

        when(crewRepository.findByCrewCode("CRW-ALPHA")).thenReturn(Optional.of(sampleCrew));

        assertThrows(BadRequestException.class, () -> crewService.createCrew(request));
        verify(crewRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test 3: Retrieve only crews marked AVAILABLE for dispatch")
    void testGetAvailableCrews() {
        when(crewRepository.findByAvailabilityStatus("AVAILABLE"))
                .thenReturn(Collections.singletonList(sampleCrew));

        List<CrewResponse> available = crewService.getAvailableCrews();

        assertNotNull(available);
        assertEquals(1, available.size());
        assertEquals("AVAILABLE", available.get(0).getAvailabilityStatus());
        verify(crewRepository, times(1)).findByAvailabilityStatus("AVAILABLE");
    }
}
