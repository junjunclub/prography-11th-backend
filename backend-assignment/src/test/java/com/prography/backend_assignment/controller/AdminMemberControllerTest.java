package com.prography.backend_assignment.controller;

import tools.jackson.databind.ObjectMapper;
import com.prography.backend_assignment.dto.admin.member.CreateMemberRequest;
import com.prography.backend_assignment.dto.admin.member.UpdateMemberRequest;
import com.prography.backend_assignment.domain.enums.Part;
import com.prography.backend_assignment.domain.enums.Role;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("회원 등록 성공")
    void createMemberSuccess() throws Exception {
        CreateMemberRequest request = new CreateMemberRequest(
                "testuser", "password123", "테스트유저", "010-1234-5678",
                Role.MEMBER, 2L, Part.SERVER, "1팀");

        mockMvc.perform(post("/admin/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.loginId").value("testuser"))
                .andExpect(jsonPath("$.data.name").value("테스트유저"))
                .andExpect(jsonPath("$.data.phone").value("010-1234-5678"))
                .andExpect(jsonPath("$.data.role").value("MEMBER"))
                .andExpect(jsonPath("$.data.part").value("SERVER"))
                .andExpect(jsonPath("$.data.team").value("1팀"))
                .andExpect(jsonPath("$.data.deposit").value(100000))
                .andExpect(jsonPath("$.data.generation").value(11))
                .andExpect(jsonPath("$.error").isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("회원 등록 실패 - loginId 중복")
    void createMemberDuplicateLoginId() throws Exception {
        CreateMemberRequest request = new CreateMemberRequest(
                "admin", "password123", "중복테스트", null,
                Role.MEMBER, 2L, null, null);

        mockMvc.perform(post("/admin/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("DUPLICATE_LOGIN_ID"));
    }

    @Test
    @Order(3)
    @DisplayName("회원 목록 조회")
    void getMembers() throws Exception {
        mockMvc.perform(get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").isNumber());
    }

    @Test
    @Order(4)
    @DisplayName("회원 상세 조회 - 존재하는 회원")
    void getMemberSuccess() throws Exception {
        mockMvc.perform(get("/admin/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.loginId").value("admin"));
    }

    @Test
    @Order(5)
    @DisplayName("회원 상세 조회 - 존재하지 않는 회원")
    void getMemberNotFound() throws Exception {
        mockMvc.perform(get("/admin/members/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("MEMBER_NOT_FOUND"));
    }

    @Test
    @Order(6)
    @DisplayName("회원 수정 성공")
    void updateMemberSuccess() throws Exception {
        UpdateMemberRequest updateReq = new UpdateMemberRequest(
                "수정완료", "010-9999-9999", Part.WEB, "2팀");

        mockMvc.perform(put("/admin/members/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("수정완료"))
                .andExpect(jsonPath("$.data.phone").value("010-9999-9999"))
                .andExpect(jsonPath("$.data.part").value("WEB"))
                .andExpect(jsonPath("$.data.team").value("2팀"));
    }

    @Test
    @Order(7)
    @DisplayName("회원 삭제(탈퇴) 성공")
    void deleteMemberSuccess() throws Exception {
        // 삭제용 회원 먼저 생성
        CreateMemberRequest createReq = new CreateMemberRequest(
                "deletetarget", "password123", "삭제대상", null,
                Role.MEMBER, 2L, null, null);
        mockMvc.perform(post("/admin/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)));

        mockMvc.perform(delete("/admin/members/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 삭제 후 상태 확인
        mockMvc.perform(get("/admin/members/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("WITHDRAWN"));
    }

    @Test
    @Order(8)
    @DisplayName("이미 탈퇴한 회원 삭제 시도")
    void deleteAlreadyWithdrawnMember() throws Exception {
        mockMvc.perform(delete("/admin/members/3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("MEMBER_ALREADY_WITHDRAWN"));
    }

    @Test
    @Order(9)
    @DisplayName("회원 등록 실패 - 필수 필드 누락")
    void createMemberValidationFail() throws Exception {
        String invalidJson = "{\"loginId\":\"\",\"password\":\"\"}";

        mockMvc.perform(post("/admin/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }
}
