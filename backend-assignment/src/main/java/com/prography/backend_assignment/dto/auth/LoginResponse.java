package com.prography.backend_assignment.dto.auth;

import com.prography.backend_assignment.domain.entity.Member;
import com.prography.backend_assignment.domain.enums.MemberStatus;
import com.prography.backend_assignment.domain.enums.Role;

import java.time.LocalDateTime;

public record LoginResponse(
        Long id,
        String loginId,
        String name,
        String phone,
        MemberStatus status,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static LoginResponse from(Member member) {
        return new LoginResponse(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getPhone(),
                member.getStatus(),
                member.getRole(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }
}
