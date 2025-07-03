package com.improvemyskills.authorizationservice.service.impl;

import com.improvemyskills.authorizationservice.entity.Permission;
import com.improvemyskills.authorizationservice.models.PermissionPayload;
import com.improvemyskills.authorizationservice.models.PermissionResponse;
import com.improvemyskills.authorizationservice.repository.PermissionRepository;
import com.improvemyskills.authorizationservice.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	PermissionRepository permissionRepository;

	public PermissionServiceImpl(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	@Override
	public PermissionResponse hasPermission(PermissionPayload permissionPaylod) {
		List<Permission> permissions = permissionRepository.findByUsernameAndActionAndResource(
				permissionPaylod.getUsername(), permissionPaylod.getAction(), permissionPaylod.getResource());

		if (permissions == null || permissions.isEmpty()){
			return PermissionResponse.builder().authorized(false).build();
		}
		return PermissionResponse.builder().authorized(true).build();
	}
}