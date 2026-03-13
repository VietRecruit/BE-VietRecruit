package com.vietrecruit.feature.application.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.application.entity.Scorecard;

@Repository
public interface ScorecardRepository extends JpaRepository<Scorecard, UUID> {

    List<Scorecard> findByInterviewId(UUID interviewId);

    boolean existsByInterviewIdAndInterviewerId(UUID interviewId, UUID interviewerId);
}
