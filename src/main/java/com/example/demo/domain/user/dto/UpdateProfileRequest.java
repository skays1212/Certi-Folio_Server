package com.example.demo.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateProfileRequest {
    private String nickname;
    private String university;
    private String major;
    private String year;
    private List<String> interests;
    private String profileImage;
}
