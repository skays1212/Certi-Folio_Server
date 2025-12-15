package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String nickname;
    private String email;
    private String university;
    private String major;
    private String year;
    private List<String> interests;
    private String profileImage;
    private String role;
    private String provider;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(String.valueOf(user.getId()))
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .university(user.getUniversity())
                .major(user.getMajor())
                .year(user.getYear())
                .interests(user.getInterests())
                .profileImage(user.getProfileImage())
                .role(user.getRoleKey())
                .provider(user.getProvider())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
