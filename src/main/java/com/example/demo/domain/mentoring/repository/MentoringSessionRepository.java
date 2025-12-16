package com.example.demo.domain.mentoring.repository;

import com.example.demo.domain.mentoring.entity.Mentor;
import com.example.demo.domain.mentoring.entity.MentoringSession;
import com.example.demo.domain.mentoring.entity.MentoringSession.SessionStatus;
import com.example.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoringSessionRepository extends JpaRepository<MentoringSession, String> {

    List<MentoringSession> findByMentor(Mentor mentor);

    List<MentoringSession> findByMentorId(Long mentorId);

    List<MentoringSession> findByMentee(User mentee);

    List<MentoringSession> findByMenteeId(Long menteeId);

    List<MentoringSession> findByStatus(SessionStatus status);

    @Query("SELECT s FROM MentoringSession s WHERE s.mentor.id = :mentorId OR s.mentee.id = :userId ORDER BY s.createdAt DESC")
    List<MentoringSession> findByMentorIdOrMenteeId(@Param("mentorId") Long mentorId, @Param("userId") Long userId);

    @Query("SELECT s FROM MentoringSession s WHERE (s.mentor.user.id = :userId OR s.mentee.id = :userId) ORDER BY s.createdAt DESC")
    List<MentoringSession> findAllSessionsForUser(@Param("userId") Long userId);
}
