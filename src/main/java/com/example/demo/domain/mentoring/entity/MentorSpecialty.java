package com.example.demo.domain.mentoring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "mentor_specialties")
public class MentorSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer level;

    @Builder
    public MentorSpecialty(Mentor mentor, String name, Integer level) {
        this.mentor = mentor;
        this.name = name;
        this.level = level;
    }
}
