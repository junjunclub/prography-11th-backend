package com.prography.backend_assignment.dto.admin.session;

import com.prography.backend_assignment.domain.entity.QrCode;

import java.time.LocalDateTime;

public record QrCodeResponse(
        Long id,
        String hashValue,
        LocalDateTime expiredAt,
        boolean active,
        Long sessionId,
        LocalDateTime createdAt) {
    public static QrCodeResponse from(QrCode qrCode) {
        return new QrCodeResponse(
                qrCode.getId(),
                qrCode.getHashValue(),
                qrCode.getExpiredAt(),
                qrCode.isActive(),
                qrCode.getSession() != null ? qrCode.getSession().getId() : null,
                qrCode.getCreatedAt());
    }
}
