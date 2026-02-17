package com.prography.backend_assignment.repository;

import com.prography.backend_assignment.domain.entity.Attendance;
import com.prography.backend_assignment.domain.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByMemberIdAndSessionId(Long memberId, Long sessionId);
    List<Attendance> findByMemberId(Long memberId);
    List<Attendance> findBySessionId(Long sessionId);
    long countByMemberIdAndStatus(Long memberId, AttendanceStatus status);
    List<Attendance> findByMemberIdAndSession_CohortId(Long memberId, Long cohortId);
}
