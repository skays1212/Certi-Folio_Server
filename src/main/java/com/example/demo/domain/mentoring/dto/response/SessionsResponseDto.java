package com.example.demo.domain.mentoring.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionsResponseDto {

    private List<MentoringSessionDto> sessions;
    private Integer total;

    public static SessionsResponseDto of(List<MentoringSessionDto> sessions) {
        return SessionsResponseDto.builder()
                .sessions(sessions)
                .total(sessions.size())
                .build();
    }
}
