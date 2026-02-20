package com.prography.backend_assignment.service;

import com.prography.backend_assignment.common.exception.BusinessException;
import com.prography.backend_assignment.common.exception.ErrorCode;
import com.prography.backend_assignment.domain.entity.Member;
import com.prography.backend_assignment.domain.enums.MemberStatus;
import com.prography.backend_assignment.dto.auth.LoginRequest;
import com.prography.backend_assignment.dto.auth.LoginResponse;
import com.prography.backend_assignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            throw new BusinessException(ErrorCode.MEMBER_WITHDRAWN);
        }

        return LoginResponse.from(member);
    }
}
