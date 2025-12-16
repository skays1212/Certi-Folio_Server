package com.example.demo.domain.mentoring.repository;

import com.example.demo.domain.mentoring.entity.Mentor;
import com.example.demo.domain.mentoring.entity.MentorReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorReviewRepository extends JpaRepository<MentorReview, Long> {

    List<MentorReview> findByMentor(Mentor mentor);

    List<MentorReview> findByMentorId(Long mentorId);

    List<MentorReview> findByMentorIdOrderByCreatedAtDesc(Long mentorId);

    @Query("SELECT AVG(r.rating) FROM MentorReview r WHERE r.mentor.id = :mentorId")
    Double calculateAverageRatingByMentorId(@Param("mentorId") Long mentorId);

    @Query("SELECT COUNT(r) FROM MentorReview r WHERE r.mentor.id = :mentorId")
    Integer countByMentorId(@Param("mentorId") Long mentorId);
}
