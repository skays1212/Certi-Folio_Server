package com.example.demo.domain.mentoring.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "mentor_time_slots")
public class MentorTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(name = "slot_date", nullable = false)
    private LocalDate date;

    @Column(name = "slot_time", nullable = false)
    private String time;

    @Column(name = "slot_type")
    private String type;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Builder
    public MentorTimeSlot(Mentor mentor, LocalDate date, String time, String type) {
        this.mentor = mentor;
        this.date = date;
        this.time = time;
        this.type = type;
        this.isAvailable = true;
    }

    public void book() {
        this.isAvailable = false;
    }

    public void release() {
        this.isAvailable = true;
    }
}
