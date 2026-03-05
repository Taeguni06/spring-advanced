package org.example.expert.domain.manager.controller;

import io.jsonwebtoken.Claims;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.manager.service.ManagerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("매니저 삭제 API 테스트 (Header 파싱 포함)")
    void deleteManager_Success() throws Exception {
        // given
        String token = "Bearer sampleToken";
        Claims claims = mock(Claims.class);
        given(claims.getSubject()).willReturn("1"); // userId를 1로 설정
        given(jwtUtil.extractClaims(anyString())).willReturn(claims);

        // when & then
        mockMvc.perform(delete("/todos/1/managers/1")
                        .header("Authorization", token))
                .andExpect(status().isOk());

        verify(managerService).deleteManager(eq(1L), eq(1L), eq(1L));
    }
}