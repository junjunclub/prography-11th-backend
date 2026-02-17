package com.prography.backend_assignment.repository;

import com.prography.backend_assignment.domain.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByHashValue(String hashValue);
    Optional<QrCode> findBySessionIdAndActiveTrue(Long sessionId);
}
