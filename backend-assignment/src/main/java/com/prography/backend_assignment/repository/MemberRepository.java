package com.prography.backend_assignment.repository;

import com.prography.backend_assignment.domain.entity.Member;
import com.prography.backend_assignment.domain.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    List<Member> findByCohortId(Long cohortId);
    Page<Member> findByCohortId(Long cohortId, Pageable pageable);
    List<Member> findByCohortIdAndStatus(Long cohortId, MemberStatus status);
    long countByCohortIdAndStatus(Long cohortId, MemberStatus status);
}
