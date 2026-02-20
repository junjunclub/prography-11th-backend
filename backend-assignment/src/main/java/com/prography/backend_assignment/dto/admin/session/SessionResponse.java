package com.prography.backend_assignment.dto.admin.session;

import com.prography.backend_assignment.domain.entity.Session;
import com.prography.backend_assignment.domain.enums.SessionStatus;

import java.time.LocalDateTime;

public record SessionResponse(
        Long id,
        String title,
        LocalDateTime startAt,
        SessionStatus status,
        Integer generation,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static SessionResponse from(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getTitle(),
                session.getStartAt(),
                session.getStatus(),
                session.getCohort() != null ? session.getCohort().getGeneration() : null,
                session.getCreatedAt(),
                session.getUpdatedAt());
    }
}
