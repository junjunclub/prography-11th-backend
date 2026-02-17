package com.prography.backend_assignment.repository;

import com.prography.backend_assignment.domain.entity.Session;
import com.prography.backend_assignment.domain.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Page<Session> findByCohortId(Long cohortId, Pageable pageable);
    List<Session> findByCohortIdAndStatusNot(Long cohortId, SessionStatus status);
    Page<Session> findByCohortIdAndStatusNot(Long cohortId, SessionStatus status, Pageable pageable);
}
