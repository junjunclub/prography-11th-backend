package com.prography.backend_assignment.service;

import com.prography.backend_assignment.common.exception.BusinessException;
import com.prography.backend_assignment.common.exception.ErrorCode;
import com.prography.backend_assignment.domain.entity.Cohort;
import com.prography.backend_assignment.domain.entity.QrCode;
import com.prography.backend_assignment.domain.entity.Session;
import com.prography.backend_assignment.domain.enums.SessionStatus;
import com.prography.backend_assignment.dto.admin.session.CreateSessionRequest;
import com.prography.backend_assignment.dto.admin.session.QrCodeResponse;
import com.prography.backend_assignment.dto.admin.session.SessionResponse;
import com.prography.backend_assignment.dto.admin.session.UpdateSessionRequest;
import com.prography.backend_assignment.repository.CohortRepository;
import com.prography.backend_assignment.repository.QrCodeRepository;
import com.prography.backend_assignment.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSessionService {

    private final SessionRepository sessionRepository;
    private final CohortRepository cohortRepository;
    private final QrCodeRepository qrCodeRepository;

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request) {
        Cohort cohort = cohortRepository.findById(request.cohortId())
                .orElseThrow(() -> new BusinessException(ErrorCode.COHORT_NOT_FOUND));

        Session session = Session.builder()
                .title(request.title())
                .startAt(request.startAt())
                .cohort(cohort)
                .build();

        Session savedSession = sessionRepository.save(session);

        // QR코드 자동 생성 (유효기간 24시간)
        qrCodeRepository.save(
                QrCode.builder()
                        .session(savedSession)
                        .hashValue(UUID.randomUUID().toString())
                        .expiredAt(LocalDateTime.now().plusHours(24))
                        .build());

        return SessionResponse.from(savedSession);
    }

    public Page<SessionResponse> getSessions(Pageable pageable) {
        return sessionRepository.findAll(pageable)
                .map(SessionResponse::from);
    }

    public SessionResponse getSession(Long id) {
        Session session = findSessionById(id);
        return SessionResponse.from(session);
    }

    public List<SessionResponse> getSessionsForMember(Long cohortId) {
        // 회원용: CANCELLED 제외
        if (cohortId != null) {
            return sessionRepository.findByCohortIdAndStatusNot(cohortId, SessionStatus.CANCELLED)
                    .stream()
                    .map(SessionResponse::from)
                    .toList();
        }
        return sessionRepository.findByStatusNot(SessionStatus.CANCELLED)
                .stream()
                .map(SessionResponse::from)
                .toList();
    }

    @Transactional
    public SessionResponse updateSession(Long id, UpdateSessionRequest request) {
        Session session = findSessionById(id);

        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.SESSION_ALREADY_CANCELLED);
        }

        session.update(request.title(), request.startAt(), request.status());
        return SessionResponse.from(session);
    }

    @Transactional
    public void deleteSession(Long id) {
        Session session = findSessionById(id);

        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.SESSION_ALREADY_CANCELLED);
        }

        session.cancel();

        // 활성 QR코드 만료 처리
        qrCodeRepository.findBySessionIdAndActiveTrue(id)
                .ifPresent(QrCode::expire);
    }

    @Transactional
    public QrCodeResponse createQrCode(Long sessionId) {
        Session session = findSessionById(sessionId);

        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.SESSION_ALREADY_CANCELLED);
        }

        // 기존 활성 QR코드 만료
        qrCodeRepository.findBySessionIdAndActiveTrue(sessionId)
                .ifPresent(QrCode::expire);

        QrCode qrCode = qrCodeRepository.save(
                QrCode.builder()
                        .session(session)
                        .hashValue(UUID.randomUUID().toString())
                        .expiredAt(LocalDateTime.now().plusHours(24))
                        .build());

        return QrCodeResponse.from(qrCode);
    }

    @Transactional
    public QrCodeResponse refreshQrCode(Long qrCodeId) {
        QrCode qrCode = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QR_NOT_FOUND));

        Session session = qrCode.getSession();

        if (session.getStatus() == SessionStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.SESSION_ALREADY_CANCELLED);
        }

        // 기존 QR 만료
        qrCode.expire();

        // 새 QR 생성
        QrCode newQrCode = qrCodeRepository.save(
                QrCode.builder()
                        .session(session)
                        .hashValue(UUID.randomUUID().toString())
                        .expiredAt(LocalDateTime.now().plusHours(24))
                        .build());

        return QrCodeResponse.from(newQrCode);
    }

    private Session findSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SESSION_NOT_FOUND));
    }
}
