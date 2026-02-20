package com.prography.backend_assignment.controller;

import tools.jackson.databind.ObjectMapper;
import com.prography.backend_assignment.dto.admin.session.CreateSessionRequest;
import com.prography.backend_assignment.dto.admin.session.UpdateSessionRequest;
import com.prography.backend_assignment.domain.enums.SessionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("일정 생성 성공")
    void createSessionSuccess() throws Exception {
        CreateSessionRequest request = new CreateSessionRequest(
                "11기 정기 세미나", LocalDateTime.of(2026, 3, 1, 14, 0), 2L);

        mockMvc.perform(post("/admin/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("11기 정기 세미나"))
                .andExpect(jsonPath("$.data.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.data.generation").value(11));
    }

    @Test
    @Order(2)
    @DisplayName("일정 목록 조회 (관리자)")
    void getSessions() throws Exception {
        mockMvc.perform(get("/admin/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").isNumber());
    }

    @Test
    @Order(3)
    @DisplayName("일정 상세 조회")
    void getSession() throws Exception {
        mockMvc.perform(get("/admin/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("11기 정기 세미나"));
    }

    @Test
    @Order(4)
    @DisplayName("일정 수정 성공")
    void updateSession() throws Exception {
        UpdateSessionRequest request = new UpdateSessionRequest(
                "수정된 세미나", LocalDateTime.of(2026, 3, 2, 15, 0), SessionStatus.IN_PROGRESS);

        mockMvc.perform(put("/admin/sessions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("수정된 세미나"))
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));
    }

    @Test
    @Order(5)
    @DisplayName("QR코드 생성 성공")
    void createQrCode() throws Exception {
        mockMvc.perform(post("/admin/sessions/1/qrcodes"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.hashValue").isString())
                .andExpect(jsonPath("$.data.active").value(true))
                .andExpect(jsonPath("$.data.sessionId").value(1));
    }

    @Test
    @Order(6)
    @DisplayName("QR코드 갱신 성공")
    void refreshQrCode() throws Exception {
        mockMvc.perform(put("/admin/qrcodes/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.hashValue").isString())
                .andExpect(jsonPath("$.data.active").value(true));
    }

    @Test
    @Order(7)
    @DisplayName("일정 삭제(취소) 성공")
    void deleteSession() throws Exception {
        // 삭제용 세션 생성
        CreateSessionRequest createReq = new CreateSessionRequest(
                "삭제 대상 세미나", LocalDateTime.of(2026, 4, 1, 10, 0), 2L);
        mockMvc.perform(post("/admin/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)));

        mockMvc.perform(delete("/admin/sessions/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 삭제 후 상태 확인
        mockMvc.perform(get("/admin/sessions/2"))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));
    }

    @Test
    @Order(8)
    @DisplayName("이미 취소된 일정 수정 시도")
    void updateCancelledSession() throws Exception {
        UpdateSessionRequest request = new UpdateSessionRequest(
                "수정 시도", LocalDateTime.now(), SessionStatus.SCHEDULED);

        mockMvc.perform(put("/admin/sessions/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("SESSION_ALREADY_CANCELLED"));
    }

    @Test
    @Order(9)
    @DisplayName("일정 상세 조회 - 존재하지 않는 일정")
    void getSessionNotFound() throws Exception {
        mockMvc.perform(get("/admin/sessions/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("SESSION_NOT_FOUND"));
    }

    @Test
    @Order(10)
    @DisplayName("회원용 일정 목록 조회 (CANCELLED 제외)")
    void getSessionsForMember() throws Exception {
        mockMvc.perform(get("/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
}
