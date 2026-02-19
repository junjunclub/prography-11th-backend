package com.prography.backend_assignment.dto.member.response;

import com.prography.backend_assignment.domain.entity.Member;

public record MemberResponseDto(
        Long id,
        String username) {
    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getId(), member.getName());
    }
}
