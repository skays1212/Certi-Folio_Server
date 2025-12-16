package com.example.demo.domain.mentoring.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateRequestResponseDto {

    private Boolean success;
    private String message;
    private Long requestId;

    public static CreateRequestResponseDto success(Long requestId) {
        return CreateRequestResponseDto.builder()
                .success(true)
                .message("멘토링 요청이 생성되었습니다.")
                .requestId(requestId)
                .build();
    }
}
