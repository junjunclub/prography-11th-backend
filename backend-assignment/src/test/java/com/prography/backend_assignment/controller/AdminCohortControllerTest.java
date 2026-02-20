package com.prography.backend_assignment.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminCohortControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("기수 목록 조회")
    void getCohorts() throws Exception {
        mockMvc.perform(get("/admin/cohorts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2)); // 10기, 11기 시드 데이터
    }

    @Test
    @DisplayName("기수 상세 조회 - 존재하는 기수")
    void getCohort() throws Exception {
        mockMvc.perform(get("/admin/cohorts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.generation").isNumber());
    }

    @Test
    @DisplayName("기수 상세 조회 - 존재하지 않는 기수")
    void getCohortNotFound() throws Exception {
        mockMvc.perform(get("/admin/cohorts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COHORT_NOT_FOUND"));
    }
}
