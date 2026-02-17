package com.prography.backend_assignment.domain.entity;

import com.prography.backend_assignment.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Attendance extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Builder.Default
    @Column(nullable = false)
    private int penaltyAmount = 0;

    private LocalDateTime scannedAt;

    // --- Domain Methods ---

    public void updateStatus(AttendanceStatus status, int penaltyAmount) {
        this.status = status;
        this.penaltyAmount = penaltyAmount;
    }
}
