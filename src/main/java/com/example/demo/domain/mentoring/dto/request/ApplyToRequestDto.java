package com.example.demo.domain.mentoring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplyToRequestDto {

    @NotNull(message = "멘토 ID는 필수입니다")
    private Long mentorId;

    @NotNull(message = "요청 ID는 필수입니다")
    private Long requestId;

    private String message;
}
