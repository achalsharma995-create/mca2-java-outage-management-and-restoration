package com.omrs.repository;

import com.omrs.entity.Feeder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeederRepository extends JpaRepository<Feeder, Long> {
    Optional<Feeder> findByFeederCode(String feederCode);
    List<Feeder> findByAreaId(Long areaId);
}
