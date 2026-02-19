package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.member.response.MemberResponseDto;
import com.prography.backend_assignment.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{id}")
    public ApiResponse<MemberResponseDto> getMemberInformation(
            @PathVariable(name = "id") String memberLoginId) {
        return ApiResponse.ok(memberService.getMemberInformation(memberLoginId));
    }
}
