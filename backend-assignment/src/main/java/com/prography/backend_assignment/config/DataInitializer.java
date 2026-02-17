package com.prography.backend_assignment.config;

import com.prography.backend_assignment.domain.entity.Cohort;
import com.prography.backend_assignment.domain.entity.DepositHistory;
import com.prography.backend_assignment.domain.entity.Member;
import com.prography.backend_assignment.domain.enums.DepositType;
import com.prography.backend_assignment.domain.enums.Role;
import com.prography.backend_assignment.repository.CohortRepository;
import com.prography.backend_assignment.repository.DepositHistoryRepository;
import com.prography.backend_assignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CohortRepository cohortRepository;
    private final MemberRepository memberRepository;
    private final DepositHistoryRepository depositHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (cohortRepository.count() > 0) {
            log.info("Seed data already exists. Skipping initialization.");
            return;
        }

        log.info("Initializing seed data...");

        // 1. 기수: 10기, 11기
        Cohort cohort10 = cohortRepository.save(
                Cohort.builder().generation(10).build()
        );
        Cohort cohort11 = cohortRepository.save(
                Cohort.builder().generation(11).build()
        );
        log.info("Created cohorts: {}기, {}기", cohort10.getGeneration(), cohort11.getGeneration());

        // 2. 관리자: loginId=admin, password=admin1234, role=ADMIN, cohort=11기
        Member admin = memberRepository.save(
                Member.builder()
                        .cohort(cohort11)
                        .loginId("admin")
                        .password(passwordEncoder.encode("admin1234"))
                        .name("관리자")
                        .role(Role.ADMIN)
                        .deposit(100_000)
                        .build()
        );
        log.info("Created admin member: loginId={}", admin.getLoginId());

        // 3. 관리자 초기 보증금 이력
        depositHistoryRepository.save(
                DepositHistory.builder()
                        .member(admin)
                        .amount(100_000)
                        .balanceAfter(100_000)
                        .type(DepositType.INITIAL)
                        .reason("초기 보증금")
                        .build()
        );
        log.info("Created initial deposit history for admin");

        log.info("Seed data initialization completed.");
    }
}
