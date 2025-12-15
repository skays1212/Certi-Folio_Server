package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.UpdateProfileRequest;
import com.example.demo.domain.user.dto.UserResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.common.ApiResponse;
import com.example.demo.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
        User user = getUserFromToken(authHeader);
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(ApiResponse.success(UserResponse.from(user)));
    }

    /**
     * 프로필 조회 (프론트엔드 호환용)
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(
            @RequestHeader("Authorization") String authHeader) {
        return getCurrentUser(authHeader);
    }

    /**
     * 프로필 수정
     */
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateProfileRequest request) {
        User user = getUserFromToken(authHeader);
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
        }

        user.updateProfile(
                request.getNickname(),
                request.getUniversity(),
                request.getMajor(),
                request.getYear(),
                request.getInterests(),
                request.getProfileImage());

        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success(UserResponse.from(user)));
    }

    /**
     * 닉네임 설정
     */
    @PutMapping("/nickname")
    public ResponseEntity<ApiResponse<Map<String, Object>>> setNickname(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request) {
        User user = getUserFromToken(authHeader);
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
        }

        String nickname = request.get("nickname");
        user.updateNickname(nickname);
        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "success", true,
                "user", Map.of("id", String.valueOf(user.getId()), "nickname", nickname))));
    }

    /**
     * 닉네임 중복 체크
     */
    @GetMapping("/nickname/check")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkNickname(
            @RequestParam String nickname) {
        boolean exists = userRepository.existsByNickname(nickname);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "available", !exists,
                "message", exists ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.")));
    }

    /**
     * 프로필 이미지 업로드
     */
    @PostMapping("/profile-image")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadProfileImage(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("image") MultipartFile file) {
        User user = getUserFromToken(authHeader);
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
        }

        // TODO: 실제 파일 업로드 로직 구현 필요 (S3, 로컬 스토리지 등)
        // 현재는 임시 URL 반환
        String imageUrl = "/uploads/profile/" + user.getId() + "_" + file.getOriginalFilename();
        user.updateProfileImage(imageUrl);
        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "success", true,
                "profile_image_url", imageUrl,
                "thumbnail_url", imageUrl)));
    }

    /**
     * 프로필 이미지 삭제
     */
    @DeleteMapping("/profile-image")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteProfileImage(
            @RequestHeader("Authorization") String authHeader) {
        User user = getUserFromToken(authHeader);
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
        }

        user.updateProfileImage(null);
        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "success", true,
                "message", "프로필 이미지가 삭제되었습니다.")));
    }

    /**
     * 계정 삭제
     */
    @DeleteMapping("/account")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteAccount(
            @RequestHeader("Authorization") String authHeader) {
        User user = getUserFromToken(authHeader);
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("사용자를 찾을 수 없습니다."));
        }

        userRepository.delete(user);

        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "success", true,
                "message", "계정이 삭제되었습니다.")));
    }

    // Helper method to extract user from token
    private User getUserFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!jwtTokenProvider.validateToken(token)) {
            return null;
        }
        String providerId = jwtTokenProvider.getEmail(token);
        return userRepository.findByProviderId(providerId).orElse(null);
    }
}
