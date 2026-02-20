package com.prography.backend_assignment.dto.admin.cohort;

import com.prography.backend_assignment.domain.entity.Cohort;

import java.time.LocalDateTime;

public record CohortResponse(
        Long id,
        int generation,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static CohortResponse from(Cohort cohort) {
        return new CohortResponse(
                cohort.getId(),
                cohort.getGeneration(),
                cohort.getCreatedAt(),
                cohort.getUpdatedAt());
    }
}
