package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Filter에서 세팅한 권한 정보 가져오기
        String userRole = (String) request.getAttribute("userRole");

        // 관리자 권한 확인
        if (userRole == null || !UserRole.ADMIN.name().equals(userRole)) {
            log.warn("미인증 사용자의 어드민 API 접근 시도: {}", request.getRequestURI());
            throw new AuthException("어드민 권한이 필요합니다.");
        }

        log.info("Admin API Access - Time: {}, URL: {}", LocalDateTime.now(), request.getRequestURI());

        return true; // 컨트롤러로 요청 전달
    }
}