package com.prography.backend_assignment.dto.attendance;

import com.prography.backend_assignment.domain.entity.DepositHistory;
import com.prography.backend_assignment.domain.enums.DepositType;

import java.time.LocalDateTime;

public record DepositHistoryResponse(
        Long id,
        int amount,
        int balanceAfter,
        DepositType type,
        String reason,
        Long attendanceId,
        LocalDateTime createdAt) {
    public static DepositHistoryResponse from(DepositHistory h) {
        return new DepositHistoryResponse(
                h.getId(),
                h.getAmount(),
                h.getBalanceAfter(),
                h.getType(),
                h.getReason(),
                h.getAttendance() != null ? h.getAttendance().getId() : null,
                h.getCreatedAt());
    }
}
