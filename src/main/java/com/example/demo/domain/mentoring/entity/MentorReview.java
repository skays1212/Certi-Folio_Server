package com.example.demo.domain.mentoring.entity;

import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "mentor_reviews")
public class MentorReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "helpful_count")
    private Integer helpfulCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Builder
    public MentorReview(Mentor mentor, User reviewer, Integer rating, String content) {
        this.mentor = mentor;
        this.reviewer = reviewer;
        this.rating = rating;
        this.content = content;
        this.helpfulCount = 0;
    }

    public void incrementHelpful() {
        this.helpfulCount++;
    }
}
