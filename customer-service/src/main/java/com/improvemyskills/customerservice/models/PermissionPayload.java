package com.improvemyskills.customerservice.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionPayload {

	private String username;
	private String action;
	private String resource;
}
