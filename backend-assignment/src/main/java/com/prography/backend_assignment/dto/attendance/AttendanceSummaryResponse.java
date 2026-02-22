package com.prography.backend_assignment.dto.attendance;

public record AttendanceSummaryResponse(
        Long memberId,
        String memberName,
        long totalSessions,
        long presentCount,
        long lateCount,
        long absentCount,
        long excusedCount,
        int currentDeposit) {
}
