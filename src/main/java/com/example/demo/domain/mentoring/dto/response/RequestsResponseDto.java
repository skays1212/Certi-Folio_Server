package com.example.demo.domain.mentoring.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RequestsResponseDto {

    private List<MentoringRequestDto> requests;
    private Integer total;

    public static RequestsResponseDto of(List<MentoringRequestDto> requests) {
        return RequestsResponseDto.builder()
                .requests(requests)
                .total(requests.size())
                .build();
    }
}
