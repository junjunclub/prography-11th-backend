package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.admin.session.SessionResponse;
import com.prography.backend_assignment.service.AdminSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

    private final AdminSessionService adminSessionService;

    @GetMapping
    public ApiResponse<List<SessionResponse>> getSessions(
            @RequestParam(required = false) Long cohortId) {
        if (cohortId != null) {
            return ApiResponse.ok(adminSessionService.getSessionsForMember(cohortId));
        }
        // cohortId 없으면 전체 세션 (CANCELLED 제외는 서비스에서 처리)
        return ApiResponse.ok(adminSessionService.getSessionsForMember(null));
    }
}
