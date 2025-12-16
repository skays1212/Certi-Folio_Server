package com.example.demo.domain.mentoring.entity;

import com.example.demo.domain.user.entity.User;
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
@Table(name = "mentors")
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String title;

    private String company;

    private String experience;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double rating = 0.0;

    @Column(name = "reviews_count")
    private Integer reviewsCount = 0;

    @ElementCollection
    @CollectionTable(name = "mentor_skills", joinColumns = @JoinColumn(name = "mentor_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    private String location;

    private String price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "mentor_achievements", joinColumns = @JoinColumn(name = "mentor_id"))
    @Column(name = "achievement")
    private List<String> achievements = new ArrayList<>();

    @Column(name = "average_response_time")
    private String averageResponseTime;

    @Column(name = "response_rate")
    private Double responseRate = 0.0;

    @Column(name = "repeat_rate")
    private Double repeatRate = 0.0;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorEducation> educations = new ArrayList<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorCareer> careers = new ArrayList<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorSpecialty> specialties = new ArrayList<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorTimeSlot> availableSlots = new ArrayList<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorReview> reviews = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Builder
    public Mentor(User user, String title, String company, String experience,
            List<String> skills, String location, String price,
            String imageUrl, String bio, String description,
            List<String> achievements) {
        this.user = user;
        this.title = title;
        this.company = company;
        this.experience = experience;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.location = location;
        this.price = price;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.description = description;
        this.achievements = achievements != null ? achievements : new ArrayList<>();
        this.rating = 0.0;
        this.reviewsCount = 0;
        this.isVerified = false;
        this.responseRate = 0.0;
        this.repeatRate = 0.0;
    }

    public void updateRating(Double newRating, int newReviewsCount) {
        this.rating = newRating;
        this.reviewsCount = newReviewsCount;
    }

    public void verify() {
        this.isVerified = true;
    }

    public void addEducation(MentorEducation education) {
        this.educations.add(education);
    }

    public void addCareer(MentorCareer career) {
        this.careers.add(career);
    }

    public void addSpecialty(MentorSpecialty specialty) {
        this.specialties.add(specialty);
    }

    public void addTimeSlot(MentorTimeSlot timeSlot) {
        this.availableSlots.add(timeSlot);
    }
}
