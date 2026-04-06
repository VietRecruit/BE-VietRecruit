package com.vietrecruit.feature.application.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.application.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    Optional<Application> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByJobIdAndCandidateIdAndDeletedAtIsNull(UUID jobId, UUID candidateId);

    /**
     * Returns a paginated list of non-deleted applications for the given company, optionally
     * filtered by job and status.
     *
     * @param companyId the owning company's UUID
     * @param jobId optional job filter; null to include all jobs
     * @param status optional status string cast to {@code application_status}; null to skip
     * @param pageable pagination and sort parameters
     * @return page of matching applications ordered by creation date descending
     */
    @Query(
            nativeQuery = true,
            value =
                    "SELECT a.* FROM applications a JOIN jobs j ON a.job_id = j.id "
                            + "WHERE j.company_id = :companyId AND a.deleted_at IS NULL "
                            + "AND (:jobId IS NULL OR a.job_id = :jobId) "
                            + "AND (CAST(:status AS application_status) IS NULL OR a.status = CAST(:status AS application_status)) "
                            + "ORDER BY a.created_at DESC",
            countQuery =
                    "SELECT COUNT(*) FROM applications a JOIN jobs j ON a.job_id = j.id "
                            + "WHERE j.company_id = :companyId AND a.deleted_at IS NULL "
                            + "AND (:jobId IS NULL OR a.job_id = :jobId) "
                            + "AND (CAST(:status AS application_status) IS NULL OR a.status = CAST(:status AS application_status))")
    Page<Application> findByCompanyFiltered(
            @Param("companyId") UUID companyId,
            @Param("jobId") UUID jobId,
            @Param("status") String status,
            Pageable pageable);

    /**
     * Returns all non-deleted applications submitted by the candidate associated with the given
     * user ID.
     *
     * @param userId the candidate's user UUID
     * @param pageable pagination and sort parameters
     * @return page of the candidate's applications
     */
    @Query(
            "SELECT a FROM Application a JOIN Candidate c ON a.candidateId = c.id "
                    + "WHERE c.userId = :userId AND a.deletedAt IS NULL")
    Page<Application> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    List<Application> findByJobIdAndDeletedAtIsNull(UUID jobId);

    List<Application> findByJobIdAndAiScoreIsNullAndDeletedAtIsNull(UUID jobId);

    Page<Application> findByJobIdAndAiScoreIsNullAndDeletedAtIsNull(UUID jobId, Pageable pageable);

    /**
     * Returns a non-deleted application by ID, validating it belongs to the given company.
     *
     * @param id the application UUID
     * @param companyId the owning company's UUID
     * @return Optional containing the application, or empty if not found or unauthorized
     */
    @Query(
            "SELECT a FROM Application a JOIN Job j ON a.jobId = j.id "
                    + "WHERE a.id = :id AND j.companyId = :companyId AND a.deletedAt IS NULL")
    Optional<Application> findByIdAndCompanyId(
            @Param("id") UUID id, @Param("companyId") UUID companyId);

    /**
     * Returns the distinct candidate IDs of all applicants who have applied to jobs at the given
     * company.
     *
     * @param companyId the company's UUID
     * @return list of distinct candidate UUIDs
     */
    @Query(
            "SELECT DISTINCT a.candidateId FROM Application a JOIN Job j ON a.jobId = j.id "
                    + "WHERE j.companyId = :companyId AND a.deletedAt IS NULL")
    List<UUID> findCandidateIdsByCompanyId(@Param("companyId") UUID companyId);
}
