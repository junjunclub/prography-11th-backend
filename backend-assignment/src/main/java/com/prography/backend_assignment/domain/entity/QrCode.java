package com.prography.backend_assignment.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class QrCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(nullable = false, unique = true)
    private String hashValue;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    // --- Domain Methods ---

    public void expire() {
        this.active = false;
    }
}
