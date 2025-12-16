package com.example.demo.domain.mentoring.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MentorsResponseDto {

    private List<MentorDto> mentors;
    private Integer total;

    public static MentorsResponseDto of(List<MentorDto> mentors) {
        return MentorsResponseDto.builder()
                .mentors(mentors)
                .total(mentors.size())
                .build();
    }
}
