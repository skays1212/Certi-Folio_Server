package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.UserResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.common.ApiResponse;
import com.example.demo.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        
        // Bearer 토큰에서 실제 토큰 추출
        String token = authHeader.replace("Bearer ", "");
        
        // 토큰 검증 및 이메일 추출
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("유효하지 않은 토큰입니다."));
        }
        
        String email = jwtTokenProvider.getEmail(token);
        log.info("사용자 정보 조회 - email: {}", email);
        
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
        }
        
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
