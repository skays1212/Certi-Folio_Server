package com.example.demo.domain.mentoring.controller;

import com.example.demo.domain.mentoring.dto.request.*;
import com.example.demo.domain.mentoring.dto.response.*;
import com.example.demo.domain.mentoring.service.MentoringService;
import com.example.demo.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mentoring", description = "멘토링 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MentoringController {

    private final MentoringService mentoringService;
    private final JwtTokenProvider jwtTokenProvider;

    // ==================== 멘토 관련 ====================

    @Operation(summary = "멘토 검색", description = "스킬과 위치로 멘토를 검색합니다.")
    @GetMapping("/mentors")
    public ResponseEntity<MentorsResponseDto> searchMentors(
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String location) {

        MentorsResponseDto response = mentoringService.searchMentors(skills, location);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "멘토 프로필 조회", description = "멘토의 상세 프로필을 조회합니다.")
    @GetMapping("/mentors/{id}")
    public ResponseEntity<MentorProfileDto> getMentorProfile(@PathVariable Long id) {
        MentorProfileDto response = mentoringService.getMentorProfile(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "멘토 신청", description = "멘토로 등록을 신청합니다.")
    @PostMapping("/mentors/apply")
    public ResponseEntity<ApiResponseDto> applyForMentor(
            HttpServletRequest request,
            @Valid @RequestBody MentorApplyRequest applyRequest) {

        Long userId = getUserIdFromRequest(request);
        ApiResponseDto response = mentoringService.applyForMentor(userId, applyRequest);
        return ResponseEntity.ok(response);
    }

    // ==================== 멘토링 요청 관련 ====================

    @Operation(summary = "멘토링 요청 목록 조회", description = "모든 멘토링 요청 목록을 조회합니다.")
    @GetMapping("/mentoring-requests")
    public ResponseEntity<RequestsResponseDto> getRequests() {
        RequestsResponseDto response = mentoringService.getRequests();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "멘토링 요청 생성", description = "새로운 멘토링 요청을 생성합니다.")
    @PostMapping("/mentoring-requests")
    public ResponseEntity<CreateRequestResponseDto> createRequest(
            HttpServletRequest request,
            @Valid @RequestBody CreateMentoringRequestDto createRequest) {

        Long userId = getUserIdFromRequest(request);
        CreateRequestResponseDto response = mentoringService.createRequest(userId, createRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "멘토링 요청에 지원", description = "멘토가 멘토링 요청에 지원합니다.")
    @PostMapping("/mentoring-requests/apply")
    public ResponseEntity<ApiResponseDto> applyToRequest(
            @Valid @RequestBody ApplyToRequestDto applyRequest) {

        ApiResponseDto response = mentoringService.applyToRequest(applyRequest);
        return ResponseEntity.ok(response);
    }

    // ==================== 멘토링 세션 관련 ====================

    @Operation(summary = "내 멘토링 세션 목록", description = "현재 사용자의 멘토링 세션 목록을 조회합니다.")
    @GetMapping("/mentoring/sessions")
    public ResponseEntity<SessionsResponseDto> getMySessions(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        SessionsResponseDto response = mentoringService.getMySessions(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "세션 상태 업데이트", description = "멘토링 세션의 상태를 업데이트합니다.")
    @PatchMapping("/mentoring/sessions/{id}/status")
    public ResponseEntity<ApiResponseDto> updateSessionStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateSessionStatusDto statusDto) {

        ApiResponseDto response = mentoringService.updateSessionStatus(id, statusDto.getStatus());
        return ResponseEntity.ok(response);
    }

    // ==================== Helper Methods ====================

    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return jwtTokenProvider.getUserId(token);
        }
        throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }
}
