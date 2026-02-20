package com.prography.backend_assignment.dto.admin.session;

import com.prography.backend_assignment.domain.enums.SessionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateSessionRequest(
        @NotBlank(message = "일정 제목을 입력해주세요") String title,

        @NotNull(message = "시작 시간을 입력해주세요") LocalDateTime startAt,

        @NotNull(message = "상태를 선택해주세요") SessionStatus status) {
}
