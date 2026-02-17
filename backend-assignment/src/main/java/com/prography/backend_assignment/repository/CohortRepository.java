package com.prography.backend_assignment.repository;

import com.prography.backend_assignment.domain.entity.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CohortRepository extends JpaRepository<Cohort, Long> {
    Optional<Cohort> findByGeneration(int generation);
}
