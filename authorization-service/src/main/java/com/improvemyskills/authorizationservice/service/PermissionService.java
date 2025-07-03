package com.improvemyskills.authorizationservice.service;

import com.improvemyskills.authorizationservice.models.PermissionPayload;
import com.improvemyskills.authorizationservice.models.PermissionResponse;

public interface PermissionService {

	PermissionResponse hasPermission(PermissionPayload permissionPaylod);
}
