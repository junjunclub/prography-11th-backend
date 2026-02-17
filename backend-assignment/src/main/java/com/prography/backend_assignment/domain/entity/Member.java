package com.prography.backend_assignment.domain.entity;

import com.prography.backend_assignment.domain.enums.MemberStatus;
import com.prography.backend_assignment.domain.enums.Part;
import com.prography.backend_assignment.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cohort_id")
    private Cohort cohort;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.MEMBER;

    @Enumerated(EnumType.STRING)
    private Part part;

    private String team;

    @Builder.Default
    @Column(nullable = false)
    private int deposit = 100_000;

    @Builder.Default
    @Column(nullable = false)
    private int excusedCount = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    // --- Domain Methods ---

    public void update(String name, Part part, String team) {
        this.name = name;
        this.part = part;
        this.team = team;
    }

    public void withdraw() {
        this.status = MemberStatus.WITHDRAWN;
    }

    public void deductDeposit(int amount) {
        this.deposit -= amount;
    }

    public void refundDeposit(int amount) {
        this.deposit += amount;
    }

    public void incrementExcusedCount() {
        this.excusedCount++;
    }

    public void decrementExcusedCount() {
        this.excusedCount--;
    }
}
