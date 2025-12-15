package com.example.demo.domain.user.entity;

import com.example.demo.domain.user.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickname;

    private String email;

    private String university;

    private String major;

    @Column(name = "academic_year")
    private String year;

    @ElementCollection
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "interest")
    private List<String> interests = new ArrayList<>();

    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false, unique = true)
    private String providerId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Builder
    public User(String name, String nickname, String email, String university, String major,
            String year, List<String> interests, String profileImage,
            Role role, String provider, String providerId) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.university = university;
        this.major = major;
        this.year = year;
        this.interests = interests != null ? interests : new ArrayList<>();
        this.profileImage = profileImage;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createdAt = LocalDateTime.now();
    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    public User updateProfile(String nickname, String university, String major,
            String year, List<String> interests, String profileImage) {
        this.nickname = nickname;
        this.university = university;
        this.major = major;
        this.year = year;
        this.interests = interests != null ? interests : this.interests;
        this.profileImage = profileImage;
        return this;
    }

    public User updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public User updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
