package com.example.demo.domain.mentoring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "mentor_educations")
public class MentorEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(nullable = false)
    private String degree;

    @Column(nullable = false)
    private String school;

    @Column(name = "graduation_year")
    private String year;

    @Builder
    public MentorEducation(Mentor mentor, String degree, String school, String year) {
        this.mentor = mentor;
        this.degree = degree;
        this.school = school;
        this.year = year;
    }
}
