package com.example.demo.domain.mentoring.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponseDto {

    private Boolean success;
    private String message;

    public static ApiResponseDto success(String message) {
        return ApiResponseDto.builder()
                .success(true)
                .message(message)
                .build();
    }

    public static ApiResponseDto error(String message) {
        return ApiResponseDto.builder()
                .success(false)
                .message(message)
                .build();
    }
}
