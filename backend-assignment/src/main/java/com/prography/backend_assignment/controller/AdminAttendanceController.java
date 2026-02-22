package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.attendance.*;
import com.prography.backend_assignment.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminAttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/attendances")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AttendanceResponse> createAttendance(
            @Valid @RequestBody CreateAttendanceRequest request) {
        return ApiResponse.created(attendanceService.createAttendance(request));
    }

    @PutMapping("/attendances/{id}")
    public ApiResponse<AttendanceResponse> updateAttendance(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAttendanceRequest request) {
        return ApiResponse.ok(attendanceService.updateAttendance(id, request));
    }

    @GetMapping("/sessions/{id}/attendances")
    public ApiResponse<List<AttendanceResponse>> getAttendancesBySession(
            @PathVariable Long id) {
        return ApiResponse.ok(attendanceService.getAttendancesBySession(id));
    }

    @GetMapping("/members/{id}/attendances")
    public ApiResponse<List<AttendanceResponse>> getAttendancesByMember(
            @PathVariable Long id) {
        return ApiResponse.ok(attendanceService.getAttendancesByMember(id));
    }

    @GetMapping("/members/{id}/attendance-summary")
    public ApiResponse<AttendanceSummaryResponse> getAttendanceSummary(
            @PathVariable Long id) {
        return ApiResponse.ok(attendanceService.getAttendanceSummary(id));
    }

    @GetMapping("/members/{id}/deposits")
    public ApiResponse<List<DepositHistoryResponse>> getDepositHistory(
            @PathVariable Long id) {
        return ApiResponse.ok(attendanceService.getDepositHistory(id));
    }
}
