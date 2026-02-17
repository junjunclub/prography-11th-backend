-- 기수: 10기, 11기
INSERT INTO cohort (generation) VALUES (10);
INSERT INTO cohort (generation) VALUES (11);

-- 관리자: loginId=admin, password=admin1234, role=ADMIN, cohort=11기(id=2)
INSERT INTO member (cohort_id, login_id, password, name, role, part, team, deposit, excused_count, status)
VALUES (2, 'admin', 'admin1234', '관리자', 'ADMIN', NULL, NULL, 100000, 0, 'ACTIVE');

-- 관리자 초기 보증금 이력
INSERT INTO deposit_history (member_id, attendance_id, amount, balance_after, type, reason)
VALUES (1, NULL, 100000, 100000, 'INITIAL', '초기 보증금');
