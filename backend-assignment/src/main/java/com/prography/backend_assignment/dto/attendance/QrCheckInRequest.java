package com.prography.backend_assignment.dto.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QrCheckInRequest(
        @NotNull(message = "회원 ID를 입력해주세요") Long memberId,

        @NotBlank(message = "QR 코드를 입력해주세요") String qrHash) {
}
