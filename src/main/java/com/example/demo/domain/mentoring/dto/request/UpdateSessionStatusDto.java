package com.example.demo.domain.mentoring.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSessionStatusDto {

    @NotBlank(message = "상태는 필수입니다")
    private String status;
}
