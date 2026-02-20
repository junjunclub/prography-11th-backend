package com.prography.backend_assignment.service;

import com.prography.backend_assignment.common.exception.BusinessException;
import com.prography.backend_assignment.common.exception.ErrorCode;
import com.prography.backend_assignment.domain.entity.Cohort;
import com.prography.backend_assignment.domain.entity.DepositHistory;
import com.prography.backend_assignment.domain.entity.Member;
import com.prography.backend_assignment.domain.enums.DepositType;
import com.prography.backend_assignment.domain.enums.MemberStatus;
import com.prography.backend_assignment.dto.admin.member.AdminMemberResponse;
import com.prography.backend_assignment.dto.admin.member.CreateMemberRequest;
import com.prography.backend_assignment.dto.admin.member.UpdateMemberRequest;
import com.prography.backend_assignment.repository.CohortRepository;
import com.prography.backend_assignment.repository.DepositHistoryRepository;
import com.prography.backend_assignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final CohortRepository cohortRepository;
    private final DepositHistoryRepository depositHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminMemberResponse createMember(CreateMemberRequest request) {
        // loginId 중복 검증
        if (memberRepository.existsByLoginId(request.loginId())) {
            throw new BusinessException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        // 기수 조회
        Cohort cohort = cohortRepository.findById(request.cohortId())
                .orElseThrow(() -> new BusinessException(ErrorCode.COHORT_NOT_FOUND));

        // 회원 생성
        Member member = Member.builder()
                .loginId(request.loginId())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .phone(request.phone())
                .role(request.role())
                .cohort(cohort)
                .part(request.part())
                .team(request.team())
                .deposit(100_000)
                .build();

        Member savedMember = memberRepository.save(member);

        // 초기 보증금 이력
        depositHistoryRepository.save(
                DepositHistory.builder()
                        .member(savedMember)
                        .amount(100_000)
                        .balanceAfter(100_000)
                        .type(DepositType.INITIAL)
                        .reason("초기 보증금")
                        .build());

        return AdminMemberResponse.from(savedMember);
    }

    public Page<AdminMemberResponse> getMembers(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(AdminMemberResponse::from);
    }

    public AdminMemberResponse getMember(Long id) {
        Member member = findMemberById(id);
        return AdminMemberResponse.from(member);
    }

    @Transactional
    public AdminMemberResponse updateMember(Long id, UpdateMemberRequest request) {
        Member member = findMemberById(id);

        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_WITHDRAWN);
        }

        member.update(request.name(), request.part(), request.team());

        if (request.phone() != null) {
            member.updatePhone(request.phone());
        }

        return AdminMemberResponse.from(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = findMemberById(id);

        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_WITHDRAWN);
        }

        member.withdraw();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
