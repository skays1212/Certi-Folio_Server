package com.example.demo.domain.mentoring.repository;

import com.example.demo.domain.mentoring.entity.Mentor;
import com.example.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Optional<Mentor> findByUser(User user);

    Optional<Mentor> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Query("SELECT m FROM Mentor m WHERE m.location = :location")
    List<Mentor> findByLocation(@Param("location") String location);

    @Query("SELECT DISTINCT m FROM Mentor m JOIN m.skills s WHERE s IN :skills")
    List<Mentor> findBySkillsIn(@Param("skills") List<String> skills);

    @Query("SELECT DISTINCT m FROM Mentor m JOIN m.skills s WHERE s IN :skills AND m.location = :location")
    List<Mentor> findBySkillsInAndLocation(@Param("skills") List<String> skills, @Param("location") String location);

    @Query("SELECT m FROM Mentor m WHERE m.isVerified = true ORDER BY m.rating DESC")
    List<Mentor> findTopVerifiedMentors();
}
