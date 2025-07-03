package com.improvemyskills.authorizationservice.repository;

import com.improvemyskills.authorizationservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

  List<Permission> findByUsernameAndActionAndResource(String username, String action, String resource);
}