package com.prography.backend_assignment.service;

import com.prography.backend_assignment.common.exception.BusinessException;
import com.prography.backend_assignment.common.exception.ErrorCode;
import com.prography.backend_assignment.dto.member.response.MemberResponseDto;
import com.prography.backend_assignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDto getMemberInformation(String memberLoginId) {
        return MemberResponseDto.of(
                memberRepository.findByLoginId(memberLoginId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)));
    }
}
