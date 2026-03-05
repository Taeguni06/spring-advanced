package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // given
        SignupRequest request = new SignupRequest("test@test.com", "password", "USER");
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

        User savedUser = new User("test@test.com", "encodedPassword", UserRole.USER);
        ReflectionTestUtils.setField(savedUser, "id", 1L); // ID 강제 주입
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        SignupResponse response = authService.signup(request);

        // then
        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시 예외 발생")
    void signup_Fail_DuplicateEmail() {
        // given
        SignupRequest request = new SignupRequest("exist@test.com", "password", "USER");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        // when & then
        assertThrows(InvalidRequestException.class, () -> authService.signup(request));
    }
}