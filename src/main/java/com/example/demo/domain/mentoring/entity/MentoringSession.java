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
@Table(name = "mentoring_sessions")
public class MentoringSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.PENDING;

    @Column(nullable = false)
    private String topic;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "total_sessions")
    private Integer totalSessions = 0;

    @Column(name = "completed_sessions")
    private Integer completedSessions = 0;

    @Column(name = "next_session_date")
    private LocalDate nextSessionDate;

    @Column(name = "next_session_time")
    private String nextSessionTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "next_session_type")
    private SessionType nextSessionType;

    private Integer progress = 0;

    @ElementCollection
    @CollectionTable(name = "session_goals", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "goal")
    private List<String> goals = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Builder
    public MentoringSession(Mentor mentor, User mentee, String topic,
            LocalDate startDate, Integer totalSessions, List<String> goals) {
        this.mentor = mentor;
        this.mentee = mentee;
        this.topic = topic;
        this.startDate = startDate;
        this.totalSessions = totalSessions != null ? totalSessions : 0;
        this.completedSessions = 0;
        this.progress = 0;
        this.goals = goals != null ? goals : new ArrayList<>();
        this.status = SessionStatus.PENDING;
    }

    public void updateStatus(SessionStatus newStatus) {
        this.status = newStatus;
    }

    public void completeSession() {
        this.completedSessions++;
        if (this.totalSessions > 0) {
            this.progress = (this.completedSessions * 100) / this.totalSessions;
        }
        if (this.completedSessions >= this.totalSessions) {
            this.status = SessionStatus.COMPLETED;
        }
    }

    public void scheduleNextSession(LocalDate date, String time, SessionType type) {
        this.nextSessionDate = date;
        this.nextSessionTime = time;
        this.nextSessionType = type;
        if (this.status == SessionStatus.PENDING) {
            this.status = SessionStatus.SCHEDULED;
        }
    }

    public void activate() {
        this.status = SessionStatus.ACTIVE;
    }

    public enum SessionStatus {
        PENDING, SCHEDULED, ACTIVE, COMPLETED, CANCELLED
    }

    public enum SessionType {
        VIDEO, OFFLINE, CHAT
    }
}
