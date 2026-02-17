package com.prography.backend_assignment.repository;

import com.prography.backend_assignment.domain.entity.DepositHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Long> {
    List<DepositHistory> findByMemberIdOrderByIdDesc(Long memberId);
}
