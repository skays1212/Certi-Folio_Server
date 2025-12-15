package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    
    private Long id;
    private String name;
    private String email;
    private String role;
    private String provider;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRoleKey())
                .provider(user.getProvider())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
