package com.example.demo.domain.mentoring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "mentor_careers")
public class MentorCareer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String company;

    private String period;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public MentorCareer(Mentor mentor, String position, String company,
            String period, String description) {
        this.mentor = mentor;
        this.position = position;
        this.company = company;
        this.period = period;
        this.description = description;
    }
}
