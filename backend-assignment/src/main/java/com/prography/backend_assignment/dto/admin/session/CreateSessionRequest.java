package com.prography.backend_assignment.dto.admin.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateSessionRequest(
        @NotBlank(message = "일정 제목을 입력해주세요") String title,

        @NotNull(message = "시작 시간을 입력해주세요") LocalDateTime startAt,

        @NotNull(message = "기수를 선택해주세요") Long cohortId) {
}
