package com.example.demo.domain.mentoring.repository;

import com.example.demo.domain.mentoring.entity.MentoringRequest;
import com.example.demo.domain.mentoring.entity.MentoringRequest.RequestStatus;
import com.example.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoringRequestRepository extends JpaRepository<MentoringRequest, Long> {

    List<MentoringRequest> findByMentee(User mentee);

    List<MentoringRequest> findByMenteeId(Long menteeId);

    List<MentoringRequest> findByStatus(RequestStatus status);

    List<MentoringRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);

    List<MentoringRequest> findAllByOrderByCreatedAtDesc();
}
