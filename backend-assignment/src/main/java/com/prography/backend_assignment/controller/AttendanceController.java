package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.attendance.AttendanceResponse;
import com.prography.backend_assignment.dto.attendance.QrCheckInRequest;
import com.prography.backend_assignment.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/attendances")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AttendanceResponse> checkIn(
            @Valid @RequestBody QrCheckInRequest request) {
        return ApiResponse.created(attendanceService.checkIn(request));
    }

    @GetMapping("/attendances")
    public ApiResponse<List<AttendanceResponse>> getMyAttendances(
            @RequestParam Long memberId) {
        return ApiResponse.ok(attendanceService.getMyAttendances(memberId));
    }
}
