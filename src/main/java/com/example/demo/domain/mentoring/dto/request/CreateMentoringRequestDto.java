package com.example.demo.domain.mentoring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateMentoringRequestDto {

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    @NotEmpty(message = "스킬은 최소 1개 이상 필요합니다")
    private List<String> skills;

    private String budget;

    private String deadline;

    private String description;
}
