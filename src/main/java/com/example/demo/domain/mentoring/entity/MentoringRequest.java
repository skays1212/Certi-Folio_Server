package com.example.demo.domain.mentoring.entity;

import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "mentoring_requests")
public class MentoringRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee;

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @CollectionTable(name = "mentoring_request_skills", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    private String budget;

    private LocalDate deadline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Builder
    public MentoringRequest(User mentee, String title, List<String> skills,
            String budget, LocalDate deadline, String description) {
        this.mentee = mentee;
        this.title = title;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.budget = budget;
        this.deadline = deadline;
        this.description = description;
        this.status = RequestStatus.ACTIVE;
    }

    public void complete() {
        this.status = RequestStatus.COMPLETED;
    }

    public enum RequestStatus {
        ACTIVE, COMPLETED
    }
}
