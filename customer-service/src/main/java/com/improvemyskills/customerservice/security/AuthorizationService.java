package com.improvemyskills.customerservice.security;

import com.improvemyskills.customerservice.models.PermissionPayload;
import com.improvemyskills.customerservice.models.PermissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service("authorizationService")
public class AuthorizationService {

	private final AuthorizationClient authorizationClient;

	public AuthorizationService(AuthorizationClient authorizationClient) {
		this.authorizationClient = authorizationClient;
	}

	public boolean hasPermission(Authentication principal, String action, String resource) throws URISyntaxException {


		//TODO 1 : Créer un client HTTP
		ResponseEntity<PermissionResponse> retour = authorizationClient.authorizePermission(new PermissionPayload(principal.getName(), action, resource));
		if (retour.getStatusCode().is2xxSuccessful()){
			return retour.getBody().isAuthorized();
		}else{
			return false;
		}
		//TODO 2 : Faire un appel vers MS AuthorizationService à L'API http://localhost:8084/api/autorize
		// en POST en lui passant l'objet {"username": "XXXX", "action": "GET / POST ", "resource" : "NOM D'UN MICROSERVICE"}
		// preferred_name
		// TODO 3 : Retourner le boolean
	}
}
