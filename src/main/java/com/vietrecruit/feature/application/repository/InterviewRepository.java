package com.vietrecruit.feature.application.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.application.entity.Interview;
import com.vietrecruit.feature.application.enums.InterviewStatus;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    Optional<Interview> findByIdAndDeletedAtIsNull(UUID id);

    /**
     * Returns a non-deleted interview by ID with its interviewers collection eagerly loaded.
     *
     * @param id the interview's UUID
     * @return Optional containing the interview with interviewers, or empty if not found
     */
    @Query(
            "SELECT DISTINCT i FROM Interview i "
                    + "LEFT JOIN FETCH i.interviewers "
                    + "WHERE i.id = :id AND i.deletedAt IS NULL")
    Optional<Interview> findByIdWithInterviewers(@Param("id") UUID id);

    List<Interview> findByApplicationIdAndDeletedAtIsNull(UUID applicationId);

    boolean existsByApplicationIdAndStatusAndDeletedAtIsNull(
            UUID applicationId, InterviewStatus status);

    /**
     * Returns all non-deleted interviews to which the given user is assigned as an interviewer,
     * ordered by scheduled time descending.
     *
     * @param userId the interviewer's user UUID
     * @return list of interviews for the interviewer
     */
    @Query(
            "SELECT i FROM Interview i JOIN i.interviewers u "
                    + "WHERE u.id = :userId AND i.deletedAt IS NULL "
                    + "ORDER BY i.scheduledAt DESC")
    List<Interview> findByInterviewerUserId(@Param("userId") UUID userId);
}
