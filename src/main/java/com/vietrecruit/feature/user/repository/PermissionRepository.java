package com.vietrecruit.feature.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vietrecruit.feature.user.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Short> {}
