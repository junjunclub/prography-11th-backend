package com.prography.backend_assignment.controller;

import com.prography.backend_assignment.common.dto.ApiResponse;
import com.prography.backend_assignment.dto.admin.cohort.CohortResponse;
import com.prography.backend_assignment.service.AdminCohortService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cohorts")
public class AdminCohortController {

    private final AdminCohortService adminCohortService;

    @GetMapping
    public ApiResponse<List<CohortResponse>> getCohorts() {
        return ApiResponse.ok(adminCohortService.getCohorts());
    }

    @GetMapping("/{id}")
    public ApiResponse<CohortResponse> getCohort(@PathVariable Long id) {
        return ApiResponse.ok(adminCohortService.getCohort(id));
    }
}
