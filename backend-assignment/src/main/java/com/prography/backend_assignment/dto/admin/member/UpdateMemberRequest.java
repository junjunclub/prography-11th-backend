package com.prography.backend_assignment.dto.admin.member;

import com.prography.backend_assignment.domain.enums.Part;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemberRequest(
        @NotBlank(message = "이름을 입력해주세요") String name,

        String phone,

        Part part,

        String team) {
}
