package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminLogAop {

    private final ObjectMapper objectMapper;

    // 어드민 컨트롤러의 모든 메서드를 타겟팅합니다.
    @Around("execution(* org.example.expert.domain..*AdminController.*(..))")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {
        // 요청 정보 추출
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long userId = (Long) request.getAttribute("userId");
        String requestUrl = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();

        // 요청 본문(RequestBody) 추출 및 JSON 변환
        Object[] args = joinPoint.getArgs();
        String requestBody = objectMapper.writeValueAsString(args.length > 0 ? args[0] : "");

        log.info(">>> Admin API Request - User ID: {}, Time: {}, URL: {}, Body: {}",
                userId, requestTime, requestUrl, requestBody);

        // 실제 메서드 실행
        Object result = joinPoint.proceed();

        // 응답 본문(ResponseBody) 추출 및 JSON 변환
        String responseBody = objectMapper.writeValueAsString(result);

        log.info("<<< Admin API Response - URL: {}, Body: {}", requestUrl, responseBody);

        return result;
    }
}