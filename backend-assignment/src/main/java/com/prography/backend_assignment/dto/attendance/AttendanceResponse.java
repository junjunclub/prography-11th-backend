package com.prography.backend_assignment.dto.attendance;

import com.prography.backend_assignment.domain.entity.Attendance;
import com.prography.backend_assignment.domain.enums.AttendanceStatus;

import java.time.LocalDateTime;

public record AttendanceResponse(
        Long id,
        Long memberId,
        String memberName,
        Long sessionId,
        String sessionTitle,
        AttendanceStatus status,
        int penaltyAmount,
        LocalDateTime scannedAt,
        LocalDateTime createdAt) {
    public static AttendanceResponse from(Attendance a) {
        return new AttendanceResponse(
                a.getId(),
                a.getMember().getId(),
                a.getMember().getName(),
                a.getSession().getId(),
                a.getSession().getTitle(),
                a.getStatus(),
                a.getPenaltyAmount(),
                a.getScannedAt(),
                a.getCreatedAt());
    }
}
