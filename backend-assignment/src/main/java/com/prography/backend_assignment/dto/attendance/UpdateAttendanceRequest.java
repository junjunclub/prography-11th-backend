package com.prography.backend_assignment.dto.attendance;

import com.prography.backend_assignment.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAttendanceRequest(
        @NotNull(message = "출결 상태를 입력해주세요") AttendanceStatus status) {
}
