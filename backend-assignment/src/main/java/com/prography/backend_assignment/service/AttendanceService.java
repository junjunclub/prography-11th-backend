package com.prography.backend_assignment.service;

import com.prography.backend_assignment.common.exception.BusinessException;
import com.prography.backend_assignment.common.exception.ErrorCode;
import com.prography.backend_assignment.domain.entity.*;
import com.prography.backend_assignment.domain.enums.AttendanceStatus;
import com.prography.backend_assignment.domain.enums.DepositType;
import com.prography.backend_assignment.domain.enums.SessionStatus;
import com.prography.backend_assignment.dto.attendance.*;
import com.prography.backend_assignment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;
    private final QrCodeRepository qrCodeRepository;
    private final DepositHistoryRepository depositHistoryRepository;

    private static final int MAX_EXCUSED = 3;
    private static final int LATE_PER_MINUTE = 500;
    private static final int MAX_PENALTY = 10_000;
    private static final int ABSENT_PENALTY = 10_000;

    // --- POST /attendances (QR 출결 체크) ---
    @Transactional
    public AttendanceResponse checkIn(QrCheckInRequest request) {
        Member member = findMemberById(request.memberId());

        QrCode qrCode = qrCodeRepository.findByHashValue(request.qrHash())
                .orElseThrow(() -> new BusinessException(ErrorCode.QR_NOT_FOUND));

        if (!qrCode.isActive()) {
            throw new BusinessException(ErrorCode.QR_INVALID);
        }
        if (qrCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.QR_EXPIRED);
        }

        Session session = qrCode.getSession();

        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.SESSION_ALREADY_CANCELLED);
        }

        // 중복 출결 체크
        attendanceRepository.findByMemberIdAndSessionId(member.getId(), session.getId())
                .ifPresent(a -> {
                    throw new BusinessException(ErrorCode.ATTENDANCE_ALREADY_CHECKED);
                });

        // 출결 판정
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startAt = session.getStartAt();
        AttendanceStatus status;
        int penalty = 0;

        if (!now.isAfter(startAt)) {
            status = AttendanceStatus.PRESENT;
        } else {
            status = AttendanceStatus.LATE;
            long minutesLate = ChronoUnit.MINUTES.between(startAt, now);
            penalty = (int) Math.min(minutesLate * LATE_PER_MINUTE, MAX_PENALTY);
        }

        Attendance attendance = attendanceRepository.save(
                Attendance.builder()
                        .member(member)
                        .session(session)
                        .status(status)
                        .penaltyAmount(penalty)
                        .scannedAt(now)
                        .build());

        // 벌금 차감
        if (penalty > 0) {
            applyPenalty(member, attendance, penalty, "지각 벌금");
        }

        return AttendanceResponse.from(attendance);
    }

    // --- GET /attendances (내 출결 기록) ---
    public List<AttendanceResponse> getMyAttendances(Long memberId) {
        return attendanceRepository.findByMemberId(memberId).stream()
                .map(AttendanceResponse::from)
                .toList();
    }

    // --- POST /admin/attendances (수동 등록) ---
    @Transactional
    public AttendanceResponse createAttendance(CreateAttendanceRequest request) {
        Member member = findMemberById(request.memberId());
        Session session = sessionRepository.findById(request.sessionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));

        attendanceRepository.findByMemberIdAndSessionId(member.getId(), session.getId())
                .ifPresent(a -> {
                    throw new BusinessException(ErrorCode.ATTENDANCE_ALREADY_CHECKED);
                });

        int penalty = calculatePenalty(request.status(), 0);

        // 공결 횟수 체크
        if (request.status() == AttendanceStatus.EXCUSED) {
            if (member.getExcusedCount() >= MAX_EXCUSED) {
                throw new BusinessException(ErrorCode.EXCUSE_LIMIT_EXCEEDED);
            }
            member.incrementExcusedCount();
        }

        Attendance attendance = attendanceRepository.save(
                Attendance.builder()
                        .member(member)
                        .session(session)
                        .status(request.status())
                        .penaltyAmount(penalty)
                        .build());

        if (penalty > 0) {
            applyPenalty(member, attendance, penalty, "출결 벌금 (" + request.status() + ")");
        }

        return AttendanceResponse.from(attendance);
    }

    // --- PUT /admin/attendances/{id} (출결 수정 + 보증금 재조정) ---
    @Transactional
    public AttendanceResponse updateAttendance(Long id, UpdateAttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ATTENDANCE_NOT_FOUND));

        Member member = attendance.getMember();
        AttendanceStatus oldStatus = attendance.getStatus();
        int oldPenalty = attendance.getPenaltyAmount();
        AttendanceStatus newStatus = request.status();
        int newPenalty = calculatePenalty(newStatus, 0);

        // 공결 횟수 조정
        if (oldStatus == AttendanceStatus.EXCUSED && newStatus != AttendanceStatus.EXCUSED) {
            member.decrementExcusedCount();
        } else if (oldStatus != AttendanceStatus.EXCUSED && newStatus == AttendanceStatus.EXCUSED) {
            if (member.getExcusedCount() >= MAX_EXCUSED) {
                throw new BusinessException(ErrorCode.EXCUSE_LIMIT_EXCEEDED);
            }
            member.incrementExcusedCount();
        }

        // 보증금 재조정: 기존 벌금 환불 → 새 벌금 차감
        if (oldPenalty > 0) {
            member.refundDeposit(oldPenalty);
            depositHistoryRepository.save(DepositHistory.builder()
                    .member(member)
                    .attendance(attendance)
                    .amount(oldPenalty)
                    .balanceAfter(member.getDeposit())
                    .type(DepositType.REFUND)
                    .reason("출결 수정 환불 (" + oldStatus + " → " + newStatus + ")")
                    .build());
        }

        attendance.updateStatus(newStatus, newPenalty);

        if (newPenalty > 0) {
            applyPenalty(member, attendance, newPenalty, "출결 수정 벌금 (" + newStatus + ")");
        }

        return AttendanceResponse.from(attendance);
    }

    // --- GET /admin/sessions/{id}/attendances ---
    public List<AttendanceResponse> getAttendancesBySession(Long sessionId) {
        sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));
        return attendanceRepository.findBySessionId(sessionId).stream()
                .map(AttendanceResponse::from)
                .toList();
    }

    // --- GET /admin/members/{id}/attendances ---
    public List<AttendanceResponse> getAttendancesByMember(Long memberId) {
        findMemberById(memberId);
        return attendanceRepository.findByMemberId(memberId).stream()
                .map(AttendanceResponse::from)
                .toList();
    }

    // --- GET /members/{id}/attendance-summary,
    // /admin/members/{id}/attendance-summary ---
    public AttendanceSummaryResponse getAttendanceSummary(Long memberId) {
        Member member = findMemberById(memberId);
        List<Attendance> attendances = attendanceRepository.findByMemberId(memberId);

        long present = attendances.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long late = attendances.stream().filter(a -> a.getStatus() == AttendanceStatus.LATE).count();
        long absent = attendances.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long excused = attendances.stream().filter(a -> a.getStatus() == AttendanceStatus.EXCUSED).count();

        return new AttendanceSummaryResponse(
                member.getId(), member.getName(),
                attendances.size(), present, late, absent, excused,
                member.getDeposit());
    }

    // --- GET /admin/members/{id}/deposits ---
    public List<DepositHistoryResponse> getDepositHistory(Long memberId) {
        findMemberById(memberId);
        return depositHistoryRepository.findByMemberIdOrderByIdDesc(memberId).stream()
                .map(DepositHistoryResponse::from)
                .toList();
    }

    // --- Private helpers ---

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private int calculatePenalty(AttendanceStatus status, long minutesLate) {
        return switch (status) {
            case PRESENT, EXCUSED -> 0;
            case LATE -> (int) Math.min(minutesLate * LATE_PER_MINUTE, MAX_PENALTY);
            case ABSENT -> ABSENT_PENALTY;
        };
    }

    private void applyPenalty(Member member, Attendance attendance, int amount, String reason) {
        member.deductDeposit(amount);
        depositHistoryRepository.save(DepositHistory.builder()
                .member(member)
                .attendance(attendance)
                .amount(-amount)
                .balanceAfter(member.getDeposit())
                .type(DepositType.PENALTY)
                .reason(reason)
                .build());
    }
}
