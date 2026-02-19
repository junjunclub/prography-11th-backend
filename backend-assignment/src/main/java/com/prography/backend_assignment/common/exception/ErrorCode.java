package com.prography.backend_assignment.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT(400, "INVALID_INPUT", "입력값이 올바르지 않습니다"),
    INTERNAL_ERROR(500, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다"),

    // Auth
    LOGIN_FAILED(401, "LOGIN_FAILED", "로그인 아이디 또는 비밀번호가 올바르지 않습니다"),

    // Member
    MEMBER_NOT_FOUND(404, "MEMBER_NOT_FOUND", "회원을 찾을 수 없습니다"),
    MEMBER_WITHDRAWN(403, "MEMBER_WITHDRAWN", "탈퇴한 회원입니다"),
    MEMBER_ALREADY_WITHDRAWN(400, "MEMBER_ALREADY_WITHDRAWN", "이미 탈퇴한 회원입니다"),
    DUPLICATE_LOGIN_ID(409, "DUPLICATE_LOGIN_ID", "이미 사용 중인 로그인 아이디입니다"),

    // Cohort
    COHORT_NOT_FOUND(404, "COHORT_NOT_FOUND", "기수를 찾을 수 없습니다"),
    PART_NOT_FOUND(404, "PART_NOT_FOUND", "파트를 찾을 수 없습니다"),
    TEAM_NOT_FOUND(404, "TEAM_NOT_FOUND", "팀을 찾을 수 없습니다"),
    COHORT_MEMBER_NOT_FOUND(404, "COHORT_MEMBER_NOT_FOUND", "기수 회원 정보를 찾을 수 없습니다"),

    // Session
    SESSION_NOT_FOUND(404, "SESSION_NOT_FOUND", "일정을 찾을 수 없습니다"),
    SESSION_ALREADY_CANCELLED(400, "SESSION_ALREADY_CANCELLED", "이미 취소된 일정입니다"),
    SESSION_NOT_IN_PROGRESS(400, "SESSION_NOT_IN_PROGRESS", "진행 중인 일정이 아닙니다"),

    // QR Code
    QR_NOT_FOUND(404, "QR_NOT_FOUND", "QR 코드를 찾을 수 없습니다"),
    QR_INVALID(400, "QR_INVALID", "유효하지 않은 QR 코드입니다"),
    QR_EXPIRED(400, "QR_EXPIRED", "만료된 QR 코드입니다"),
    QR_ALREADY_ACTIVE(409, "QR_ALREADY_ACTIVE", "이미 활성화된 QR 코드가 있습니다"),

    // Attendance
    ATTENDANCE_NOT_FOUND(404, "ATTENDANCE_NOT_FOUND", "출결 기록을 찾을 수 없습니다"),
    ATTENDANCE_ALREADY_CHECKED(409, "ATTENDANCE_ALREADY_CHECKED", "이미 출결 체크가 완료되었습니다"),

    // Deposit
    DEPOSIT_INSUFFICIENT(400, "DEPOSIT_INSUFFICIENT", "보증금 잔액이 부족합니다"),

    // Excused
    EXCUSE_LIMIT_EXCEEDED(400, "EXCUSE_LIMIT_EXCEEDED", "공결 횟수를 초과했습니다 (최대 3회)");

    private final int status;
    private final String code;
    private final String message;
}
