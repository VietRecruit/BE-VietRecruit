package com.vietrecruit.feature.application.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.application.entity.ApplicationStatusHistory;

@Repository
public interface ApplicationStatusHistoryRepository
        extends JpaRepository<ApplicationStatusHistory, UUID> {

    List<ApplicationStatusHistory> findByApplicationIdOrderByChangedAtDesc(UUID applicationId);
}
