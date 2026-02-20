package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.admin.member.AdminMemberResponse;
import com.prography.backend_assignment.dto.admin.member.CreateMemberRequest;
import com.prography.backend_assignment.dto.admin.member.UpdateMemberRequest;
import com.prography.backend_assignment.service.AdminMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AdminMemberResponse> createMember(
            @Valid @RequestBody CreateMemberRequest request) {
        return ApiResponse.created(adminMemberService.createMember(request));
    }

    @GetMapping
    public ApiResponse<Page<AdminMemberResponse>> getMembers(
            @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.ok(adminMemberService.getMembers(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminMemberResponse> getMember(@PathVariable Long id) {
        return ApiResponse.ok(adminMemberService.getMember(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<AdminMemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMemberRequest request) {
        return ApiResponse.ok(adminMemberService.updateMember(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMember(@PathVariable Long id) {
        adminMemberService.deleteMember(id);
        return ApiResponse.ok();
    }
}
