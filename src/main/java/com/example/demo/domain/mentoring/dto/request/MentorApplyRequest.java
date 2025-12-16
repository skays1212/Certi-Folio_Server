package com.example.demo.domain.mentoring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MentorApplyRequest {

    @NotBlank(message = "직함은 필수입니다")
    private String title;

    private String company;

    private String experience;

    @NotEmpty(message = "스킬은 최소 1개 이상 필요합니다")
    private List<String> skills;

    private String location;

    private String price;

    private String bio;

    private String description;

    private List<String> achievements;

    private List<EducationDto> educations;

    private List<CareerDto> careers;

    private List<SpecialtyDto> specialties;

    @Getter
    @NoArgsConstructor
    public static class EducationDto {
        private String degree;
        private String school;
        private String year;
    }

    @Getter
    @NoArgsConstructor
    public static class CareerDto {
        private String position;
        private String company;
        private String period;
        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class SpecialtyDto {
        private String name;
        private Integer level;
    }
}
