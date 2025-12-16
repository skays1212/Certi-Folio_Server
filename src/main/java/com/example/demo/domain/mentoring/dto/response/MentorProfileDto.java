package com.example.demo.domain.mentoring.dto.response;

import com.example.demo.domain.mentoring.entity.*;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MentorProfileDto {

    private Long id;
    private String name;
    private String title;
    private String company;
    private String experience;
    private Double rating;
    private Integer reviewsCount;
    private List<String> skills;
    private String location;
    private String price;
    private String imageUrl;
    private Boolean isVerified;
    private String bio;
    private List<EducationDto> education;
    private List<CareerDto> career;
    private List<String> achievements;
    private List<SpecialtyDto> specialties;
    private List<TimeSlotDto> availableSlots;
    private List<ReviewDto> reviews;
    private ResponseStatsDto responseStats;

    @Getter
    @Builder
    public static class EducationDto {
        private String degree;
        private String school;
        private String year;

        public static EducationDto from(MentorEducation education) {
            return EducationDto.builder()
                    .degree(education.getDegree())
                    .school(education.getSchool())
                    .year(education.getYear())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CareerDto {
        private String position;
        private String company;
        private String period;
        private String description;

        public static CareerDto from(MentorCareer career) {
            return CareerDto.builder()
                    .position(career.getPosition())
                    .company(career.getCompany())
                    .period(career.getPeriod())
                    .description(career.getDescription())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SpecialtyDto {
        private String name;
        private Integer level;

        public static SpecialtyDto from(MentorSpecialty specialty) {
            return SpecialtyDto.builder()
                    .name(specialty.getName())
                    .level(specialty.getLevel())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class TimeSlotDto {
        private String date;
        private String time;
        private String type;

        public static TimeSlotDto from(MentorTimeSlot slot) {
            return TimeSlotDto.builder()
                    .date(slot.getDate().toString())
                    .time(slot.getTime())
                    .type(slot.getType())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ReviewDto {
        private Long id;
        private String reviewer;
        private Integer rating;
        private String date;
        private String content;
        private Integer helpfulCount;

        public static ReviewDto from(MentorReview review) {
            return ReviewDto.builder()
                    .id(review.getId())
                    .reviewer(review.getReviewer().getNickname() != null ? review.getReviewer().getNickname()
                            : review.getReviewer().getName())
                    .rating(review.getRating())
                    .date(review.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE))
                    .content(review.getContent())
                    .helpfulCount(review.getHelpfulCount())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseStatsDto {
        private String averageResponseTime;
        private Double responseRate;
        private Double repeatRate;
    }

    public static MentorProfileDto from(Mentor mentor) {
        return MentorProfileDto.builder()
                .id(mentor.getId())
                .name(mentor.getUser().getNickname() != null ? mentor.getUser().getNickname()
                        : mentor.getUser().getName())
                .title(mentor.getTitle())
                .company(mentor.getCompany())
                .experience(mentor.getExperience())
                .rating(mentor.getRating())
                .reviewsCount(mentor.getReviewsCount())
                .skills(mentor.getSkills())
                .location(mentor.getLocation())
                .price(mentor.getPrice())
                .imageUrl(mentor.getImageUrl() != null ? mentor.getImageUrl() : mentor.getUser().getProfileImage())
                .isVerified(mentor.getIsVerified())
                .bio(mentor.getBio())
                .education(mentor.getEducations().stream()
                        .map(EducationDto::from)
                        .collect(Collectors.toList()))
                .career(mentor.getCareers().stream()
                        .map(CareerDto::from)
                        .collect(Collectors.toList()))
                .achievements(mentor.getAchievements())
                .specialties(mentor.getSpecialties().stream()
                        .map(SpecialtyDto::from)
                        .collect(Collectors.toList()))
                .availableSlots(mentor.getAvailableSlots().stream()
                        .filter(MentorTimeSlot::getIsAvailable)
                        .map(TimeSlotDto::from)
                        .collect(Collectors.toList()))
                .reviews(mentor.getReviews().stream()
                        .map(ReviewDto::from)
                        .collect(Collectors.toList()))
                .responseStats(ResponseStatsDto.builder()
                        .averageResponseTime(mentor.getAverageResponseTime())
                        .responseRate(mentor.getResponseRate())
                        .repeatRate(mentor.getRepeatRate())
                        .build())
                .build();
    }
}
