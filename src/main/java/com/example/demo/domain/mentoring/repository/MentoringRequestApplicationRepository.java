package com.example.demo.domain.mentoring.repository;

import com.example.demo.domain.mentoring.entity.Mentor;
import com.example.demo.domain.mentoring.entity.MentoringRequest;
import com.example.demo.domain.mentoring.entity.MentoringRequestApplication;
import com.example.demo.domain.mentoring.entity.MentoringRequestApplication.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentoringRequestApplicationRepository extends JpaRepository<MentoringRequestApplication, Long> {

    List<MentoringRequestApplication> findByMentor(Mentor mentor);

    List<MentoringRequestApplication> findByMentorId(Long mentorId);

    List<MentoringRequestApplication> findByRequest(MentoringRequest request);

    List<MentoringRequestApplication> findByRequestId(Long requestId);

    Optional<MentoringRequestApplication> findByMentorIdAndRequestId(Long mentorId, Long requestId);

    boolean existsByMentorIdAndRequestId(Long mentorId, Long requestId);

    List<MentoringRequestApplication> findByStatus(ApplicationStatus status);
}
