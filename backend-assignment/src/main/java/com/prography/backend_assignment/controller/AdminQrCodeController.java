package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.admin.session.QrCodeResponse;
import com.prography.backend_assignment.service.AdminSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/qrcodes")
public class AdminQrCodeController {

    private final AdminSessionService adminSessionService;

    @PutMapping("/{id}")
    public ApiResponse<QrCodeResponse> refreshQrCode(@PathVariable Long id) {
        return ApiResponse.ok(adminSessionService.refreshQrCode(id));
    }
}
