package com.improvemyskills.authorizationservice.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponse {
	private boolean authorized;
}
