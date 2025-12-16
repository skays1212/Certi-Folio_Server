package com.example.demo.domain.mentoring.dto.response;

import com.example.demo.domain.mentoring.entity.MentoringRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MentoringRequestDto {

    private Long id;
    private String title;
    private String mentee;
    private List<String> skills;
    private String budget;
    private String deadline;
    private String description;
    private String status;

    public static MentoringRequestDto from(MentoringRequest request) {
        return MentoringRequestDto.builder()
                .id(request.getId())
                .title(request.getTitle())
                .mentee(request.getMentee().getNickname() != null ? request.getMentee().getNickname()
                        : request.getMentee().getName())
                .skills(request.getSkills())
                .budget(request.getBudget())
                .deadline(request.getDeadline() != null ? request.getDeadline().toString() : null)
                .description(request.getDescription())
                .status(request.getStatus().name().toLowerCase())
                .build();
    }
}
