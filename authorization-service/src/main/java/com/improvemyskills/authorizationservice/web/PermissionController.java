package com.improvemyskills.authorizationservice.web;

import com.improvemyskills.authorizationservice.models.PermissionPayload;
import com.improvemyskills.authorizationservice.models.PermissionResponse;
import com.improvemyskills.authorizationservice.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PermissionController {

	PermissionService permissionService;

	public PermissionController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@PostMapping("/authorize")
	ResponseEntity<PermissionResponse> authorizePermission(@RequestBody PermissionPayload permissionPayload){
		return ResponseEntity.ok(
				permissionService.hasPermission(permissionPayload)
		);
	}
}