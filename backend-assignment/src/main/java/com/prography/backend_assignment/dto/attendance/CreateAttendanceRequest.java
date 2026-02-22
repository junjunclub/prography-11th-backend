package com.prography.backend_assignment.dto.attendance;

import com.prography.backend_assignment.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record CreateAttendanceRequest(
        @NotNull(message = "회원 ID를 입력해주세요") Long memberId,

        @NotNull(message = "세션 ID를 입력해주세요") Long sessionId,

        @NotNull(message = "출결 상태를 입력해주세요") AttendanceStatus status) {
}
