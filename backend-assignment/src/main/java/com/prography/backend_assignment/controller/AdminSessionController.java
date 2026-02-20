package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.admin.session.CreateSessionRequest;
import com.prography.backend_assignment.dto.admin.session.QrCodeResponse;
import com.prography.backend_assignment.dto.admin.session.SessionResponse;
import com.prography.backend_assignment.dto.admin.session.UpdateSessionRequest;
import com.prography.backend_assignment.service.AdminSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sessions")
public class AdminSessionController {

    private final AdminSessionService adminSessionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SessionResponse> createSession(
            @Valid @RequestBody CreateSessionRequest request) {
        return ApiResponse.created(adminSessionService.createSession(request));
    }

    @GetMapping
    public ApiResponse<Page<SessionResponse>> getSessions(
            @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.ok(adminSessionService.getSessions(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<SessionResponse> getSession(@PathVariable Long id) {
        return ApiResponse.ok(adminSessionService.getSession(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<SessionResponse> updateSession(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSessionRequest request) {
        return ApiResponse.ok(adminSessionService.updateSession(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSession(@PathVariable Long id) {
        adminSessionService.deleteSession(id);
        return ApiResponse.ok();
    }

    @PostMapping("/{id}/qrcodes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<QrCodeResponse> createQrCode(@PathVariable Long id) {
        return ApiResponse.created(adminSessionService.createQrCode(id));
    }
}
