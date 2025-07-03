package com.improvemyskills.customerservice.security;

import com.improvemyskills.customerservice.models.PermissionPayload;
import com.improvemyskills.customerservice.models.PermissionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTHORIZATION-SERVICE")
public interface AuthorizationClient {

	@PostMapping("/api/authorize")
	ResponseEntity<PermissionResponse> authorizePermission(@RequestBody PermissionPayload permissionPayload);
}
