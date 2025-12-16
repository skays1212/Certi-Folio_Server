package com.example.demo.domain.mentoring.service;

import com.example.demo.domain.mentoring.dto.request.*;
import com.example.demo.domain.mentoring.dto.response.*;
import com.example.demo.domain.mentoring.entity.*;
import com.example.demo.domain.mentoring.repository.*;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentoringService {

    private final MentorRepository mentorRepository;
    private final MentoringRequestRepository mentoringRequestRepository;
    private final MentoringSessionRepository mentoringSessionRepository;
    private final MentorReviewRepository mentorReviewRepository;
    private final MentoringRequestApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    // ==================== 멘토 관련 ====================

    /**
     * 멘토 검색
     */
    public MentorsResponseDto searchMentors(List<String> skills, String location) {
        List<Mentor> mentors;

        if (skills != null && !skills.isEmpty() && location != null && !location.isEmpty()) {
            mentors = mentorRepository.findBySkillsInAndLocation(skills, location);
        } else if (skills != null && !skills.isEmpty()) {
            mentors = mentorRepository.findBySkillsIn(skills);
        } else if (location != null && !location.isEmpty()) {
            mentors = mentorRepository.findByLocation(location);
        } else {
            mentors = mentorRepository.findAll();
        }

        List<MentorDto> mentorDtos = mentors.stream()
                .map(MentorDto::from)
                .collect(Collectors.toList());

        return MentorsResponseDto.of(mentorDtos);
    }

    /**
     * 멘토 프로필 조회
     */
    public MentorProfileDto getMentorProfile(Long mentorId) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("멘토를 찾을 수 없습니다."));

        return MentorProfileDto.from(mentor);
    }

    /**
     * 멘토 신청
     */
    @Transactional
    public ApiResponseDto applyForMentor(Long userId, MentorApplyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (mentorRepository.existsByUserId(userId)) {
            throw new IllegalStateException("이미 멘토로 등록되어 있습니다.");
        }

        Mentor mentor = Mentor.builder()
                .user(user)
                .title(request.getTitle())
                .company(request.getCompany())
                .experience(request.getExperience())
                .skills(request.getSkills())
                .location(request.getLocation())
                .price(request.getPrice())
                .bio(request.getBio())
                .description(request.getDescription())
                .achievements(request.getAchievements())
                .build();

        // 학력 추가
        if (request.getEducations() != null) {
            for (MentorApplyRequest.EducationDto eduDto : request.getEducations()) {
                MentorEducation education = MentorEducation.builder()
                        .mentor(mentor)
                        .degree(eduDto.getDegree())
                        .school(eduDto.getSchool())
                        .year(eduDto.getYear())
                        .build();
                mentor.addEducation(education);
            }
        }

        // 경력 추가
        if (request.getCareers() != null) {
            for (MentorApplyRequest.CareerDto careerDto : request.getCareers()) {
                MentorCareer career = MentorCareer.builder()
                        .mentor(mentor)
                        .position(careerDto.getPosition())
                        .company(careerDto.getCompany())
                        .period(careerDto.getPeriod())
                        .description(careerDto.getDescription())
                        .build();
                mentor.addCareer(career);
            }
        }

        // 전문 분야 추가
        if (request.getSpecialties() != null) {
            for (MentorApplyRequest.SpecialtyDto specDto : request.getSpecialties()) {
                MentorSpecialty specialty = MentorSpecialty.builder()
                        .mentor(mentor)
                        .name(specDto.getName())
                        .level(specDto.getLevel())
                        .build();
                mentor.addSpecialty(specialty);
            }
        }

        mentorRepository.save(mentor);

        return ApiResponseDto.success("멘토 신청이 완료되었습니다.");
    }

    // ==================== 멘토링 요청 관련 ====================

    /**
     * 멘토링 요청 목록 조회
     */
    public RequestsResponseDto getRequests() {
        List<MentoringRequest> requests = mentoringRequestRepository.findAllByOrderByCreatedAtDesc();

        List<MentoringRequestDto> requestDtos = requests.stream()
                .map(MentoringRequestDto::from)
                .collect(Collectors.toList());

        return RequestsResponseDto.of(requestDtos);
    }

    /**
     * 멘토링 요청 생성
     */
    @Transactional
    public CreateRequestResponseDto createRequest(Long userId, CreateMentoringRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LocalDate deadline = null;
        if (dto.getDeadline() != null && !dto.getDeadline().isEmpty()) {
            deadline = LocalDate.parse(dto.getDeadline(), DateTimeFormatter.ISO_LOCAL_DATE);
        }

        MentoringRequest request = MentoringRequest.builder()
                .mentee(user)
                .title(dto.getTitle())
                .skills(dto.getSkills())
                .budget(dto.getBudget())
                .deadline(deadline)
                .description(dto.getDescription())
                .build();

        MentoringRequest savedRequest = mentoringRequestRepository.save(request);

        return CreateRequestResponseDto.success(savedRequest.getId());
    }

    /**
     * 멘토링 요청에 지원
     */
    @Transactional
    public ApiResponseDto applyToRequest(ApplyToRequestDto dto) {
        Mentor mentor = mentorRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new IllegalArgumentException("멘토를 찾을 수 없습니다."));

        MentoringRequest request = mentoringRequestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("멘토링 요청을 찾을 수 없습니다."));

        if (applicationRepository.existsByMentorIdAndRequestId(dto.getMentorId(), dto.getRequestId())) {
            throw new IllegalStateException("이미 지원한 요청입니다.");
        }

        MentoringRequestApplication application = MentoringRequestApplication.builder()
                .mentor(mentor)
                .request(request)
                .message(dto.getMessage())
                .build();

        applicationRepository.save(application);

        return ApiResponseDto.success("멘토링 요청에 지원했습니다.");
    }

    // ==================== 멘토링 세션 관련 ====================

    /**
     * 내 멘토링 세션 목록 조회
     */
    public SessionsResponseDto getMySessions(Long userId) {
        List<MentoringSession> sessions = mentoringSessionRepository.findAllSessionsForUser(userId);

        List<MentoringSessionDto> sessionDtos = sessions.stream()
                .map(MentoringSessionDto::from)
                .collect(Collectors.toList());

        return SessionsResponseDto.of(sessionDtos);
    }

    /**
     * 세션 상태 업데이트
     */
    @Transactional
    public ApiResponseDto updateSessionStatus(String sessionId, String status) {
        MentoringSession session = mentoringSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        MentoringSession.SessionStatus newStatus = MentoringSession.SessionStatus.valueOf(status.toUpperCase());
        session.updateStatus(newStatus);

        return ApiResponseDto.success("세션 상태가 업데이트되었습니다.");
    }

    // ==================== 내 멘토 정보 조회 ====================

    /**
     * 현재 사용자의 멘토 ID 조회
     */
    public Long getMyMentorId(Long userId) {
        return mentorRepository.findByUserId(userId)
                .map(Mentor::getId)
                .orElse(null);
    }
}
