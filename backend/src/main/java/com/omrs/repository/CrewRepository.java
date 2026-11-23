package com.omrs.repository;

import com.omrs.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
    Optional<Crew> findByCrewCode(String crewCode);
    List<Crew> findByAvailabilityStatus(String availabilityStatus);
    long countByAvailabilityStatus(String availabilityStatus);
    List<Crew> findByBaseAreaId(Long baseAreaId);
}
