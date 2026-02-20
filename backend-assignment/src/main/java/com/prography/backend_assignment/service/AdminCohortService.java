package com.prography.backend_assignment.service;

import com.prography.backend_assignment.common.exception.BusinessException;
import com.prography.backend_assignment.common.exception.ErrorCode;
import com.prography.backend_assignment.dto.admin.cohort.CohortResponse;
import com.prography.backend_assignment.repository.CohortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCohortService {

    private final CohortRepository cohortRepository;

    public List<CohortResponse> getCohorts() {
        return cohortRepository.findAll().stream()
                .map(CohortResponse::from)
                .toList();
    }

    public CohortResponse getCohort(Long id) {
        return CohortResponse.from(
                cohortRepository.findById(id)
                        .orElseThrow(() -> new BusinessException(ErrorCode.COHORT_NOT_FOUND)));
    }
}
