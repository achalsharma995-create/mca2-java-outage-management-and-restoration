package com.omrs.service;

import com.omrs.dto.crew.CrewMemberDto;
import com.omrs.dto.crew.CrewRequest;
import com.omrs.dto.crew.CrewResponse;
import com.omrs.entity.Area;
import com.omrs.entity.Crew;
import com.omrs.entity.CrewMember;
import com.omrs.entity.User;
import com.omrs.exception.BadRequestException;
import com.omrs.exception.ResourceNotFoundException;
import com.omrs.repository.AreaRepository;
import com.omrs.repository.CrewMemberRepository;
import com.omrs.repository.CrewRepository;
import com.omrs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrewServiceImpl implements CrewService {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public CrewResponse createCrew(CrewRequest request) {
        if (crewRepository.findByCrewCode(request.getCrewCode()).isPresent()) {
            throw new BadRequestException("Crew code already exists: " + request.getCrewCode());
        }

        Area baseArea = areaRepository.findById(request.getBaseAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Area", "id", request.getBaseAreaId()));

        User leadEngineer = null;
        if (request.getLeadEngineerId() != null) {
            leadEngineer = userRepository.findById(request.getLeadEngineerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getLeadEngineerId()));
        }

        Crew crew = new Crew();
        crew.setCrewCode(request.getCrewCode());
        crew.setCrewName(request.getCrewName());
        crew.setLeadEngineer(leadEngineer);
        crew.setVehicleNumber(request.getVehicleNumber());
        crew.setVehicleType(request.getVehicleType() != null ? request.getVehicleType() : "Bucket Truck");
        crew.setContactPhone(request.getContactPhone());
        crew.setAvailabilityStatus(request.getAvailabilityStatus() != null ? request.getAvailabilityStatus() : "AVAILABLE");
        crew.setBaseArea(baseArea);
        crew.setMembersCount(request.getMembersCount() != null ? request.getMembersCount() : 3);
        crew.setSkillSpecialization(request.getSkillSpecialization());

        Crew savedCrew = crewRepository.save(crew);

        if (request.getMembers() != null && !request.getMembers().isEmpty()) {
            for (CrewMemberDto mDto : request.getMembers()) {
                CrewMember member = new CrewMember();
                member.setCrew(savedCrew);
                member.setMemberName(mDto.getMemberName());
                member.setRoleDesignation(mDto.getRoleDesignation());
                member.setPhone(mDto.getPhone());
                if (mDto.getUserId() != null) {
                    userRepository.findById(mDto.getUserId()).ifPresent(member::setUser);
                }
                crewMemberRepository.save(member);
            }
        }

        return mapToResponse(savedCrew);
    }

    @Override
    @Transactional
    public CrewResponse updateCrew(Long crewId, CrewRequest request) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ResourceNotFoundException("Crew", "id", crewId));

        if (request.getBaseAreaId() != null) {
            Area baseArea = areaRepository.findById(request.getBaseAreaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Area", "id", request.getBaseAreaId()));
            crew.setBaseArea(baseArea);
        }

        if (request.getLeadEngineerId() != null) {
            User lead = userRepository.findById(request.getLeadEngineerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getLeadEngineerId()));
            crew.setLeadEngineer(lead);
        }

        crew.setCrewName(request.getCrewName());
        crew.setVehicleNumber(request.getVehicleNumber());
        if (request.getVehicleType() != null) crew.setVehicleType(request.getVehicleType());
        crew.setContactPhone(request.getContactPhone());
        if (request.getAvailabilityStatus() != null) crew.setAvailabilityStatus(request.getAvailabilityStatus());
        if (request.getMembersCount() != null) crew.setMembersCount(request.getMembersCount());
        if (request.getSkillSpecialization() != null) crew.setSkillSpecialization(request.getSkillSpecialization());

        Crew updated = crewRepository.save(crew);
        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public CrewResponse getCrewById(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ResourceNotFoundException("Crew", "id", crewId));
        return mapToResponse(crew);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CrewResponse> getAllCrews() {
        return crewRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CrewResponse> getAvailableCrews() {
        return crewRepository.findByAvailabilityStatus("AVAILABLE").stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCrew(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ResourceNotFoundException("Crew", "id", crewId));
        crewRepository.delete(crew);
    }

    @Override
    @Transactional
    public void updateCrewStatus(Long crewId, String availabilityStatus) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ResourceNotFoundException("Crew", "id", crewId));
        crew.setAvailabilityStatus(availabilityStatus);
        crewRepository.save(crew);
    }

    public CrewResponse mapToResponse(Crew crew) {
        CrewResponse dto = new CrewResponse();
        dto.setId(crew.getId());
        dto.setCrewCode(crew.getCrewCode());
        dto.setCrewName(crew.getCrewName());
        if (crew.getLeadEngineer() != null) {
            dto.setLeadEngineerId(crew.getLeadEngineer().getId());
            dto.setLeadEngineerName(crew.getLeadEngineer().getFullName());
        }
        dto.setVehicleNumber(crew.getVehicleNumber());
        dto.setVehicleType(crew.getVehicleType());
        dto.setContactPhone(crew.getContactPhone());
        dto.setAvailabilityStatus(crew.getAvailabilityStatus());
        dto.setBaseAreaId(crew.getBaseArea().getId());
        dto.setBaseAreaName(crew.getBaseArea().getAreaName());
        dto.setMembersCount(crew.getMembersCount());
        dto.setSkillSpecialization(crew.getSkillSpecialization());
        dto.setCreatedAt(crew.getCreatedAt());

        List<CrewMember> members = crewMemberRepository.findByCrewId(crew.getId());
        List<CrewMemberDto> memberDtos = members.stream()
                .map(m -> new CrewMemberDto(m.getId(), m.getUser() != null ? m.getUser().getId() : null, m.getMemberName(), m.getRoleDesignation(), m.getPhone()))
                .collect(Collectors.toList());
        dto.setMembers(memberDtos);

        return dto;
    }
}
