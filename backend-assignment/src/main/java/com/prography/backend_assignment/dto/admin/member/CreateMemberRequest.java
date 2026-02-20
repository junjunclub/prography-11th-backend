package com.prography.backend_assignment.dto.admin.member;

import com.prography.backend_assignment.domain.enums.Part;
import com.prography.backend_assignment.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMemberRequest(
        @NotBlank(message = "로그인 아이디를 입력해주세요") String loginId,

        @NotBlank(message = "비밀번호를 입력해주세요") String password,

        @NotBlank(message = "이름을 입력해주세요") String name,

        String phone,

        @NotNull(message = "역할을 선택해주세요") Role role,

        @NotNull(message = "기수를 선택해주세요") Long cohortId,

        Part part,

        String team) {
}
