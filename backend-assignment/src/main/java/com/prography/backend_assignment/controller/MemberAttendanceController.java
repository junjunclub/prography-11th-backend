package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.attendance.AttendanceSummaryResponse;
import com.prography.backend_assignment.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberAttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/{id}/attendance-summary")
    public ApiResponse<AttendanceSummaryResponse> getAttendanceSummary(
            @PathVariable Long id) {
        return ApiResponse.ok(attendanceService.getAttendanceSummary(id));
    }
}
