package com.prography.backend_assignment.dto.admin.member;

import com.prography.backend_assignment.domain.entity.Member;
import com.prography.backend_assignment.domain.enums.MemberStatus;
import com.prography.backend_assignment.domain.enums.Part;
import com.prography.backend_assignment.domain.enums.Role;

import java.time.LocalDateTime;

public record AdminMemberResponse(
        Long id,
        String loginId,
        String name,
        String phone,
        MemberStatus status,
        Role role,
        Integer generation,
        Part part,
        String team,
        int deposit,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static AdminMemberResponse from(Member member) {
        return new AdminMemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getPhone(),
                member.getStatus(),
                member.getRole(),
                member.getCohort() != null ? member.getCohort().getGeneration() : null,
                member.getPart(),
                member.getTeam(),
                member.getDeposit(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }
}
