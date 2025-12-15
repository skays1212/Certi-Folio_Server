package com.example.demo.global.auth;

import com.example.demo.global.common.ApiResponse;
import com.example.demo.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refreshToken(
            @RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");

        // 실제 구현에서는 refresh token 검증 및 새 토큰 발급 로직 필요
        // 현재는 간단한 구현
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("유효하지 않은 리프레시 토큰입니다."));
        }

        // TODO: 실제 리프레시 토큰 검증 로직 구현
        String newAccessToken = jwtTokenProvider.createAccessToken("refreshed_user");

        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "access_token", newAccessToken,
                "refresh_token", refreshToken,
                "expires_in", 3600)));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Map<String, Object>>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // JWT 기반이므로 서버에서 토큰을 무효화하려면 블랙리스트 관리 필요
        // 현재는 클라이언트에서 토큰을 삭제하는 것으로 처리
        log.info("로그아웃 요청");

        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "success", true,
                "message", "로그아웃되었습니다.")));
    }
}
