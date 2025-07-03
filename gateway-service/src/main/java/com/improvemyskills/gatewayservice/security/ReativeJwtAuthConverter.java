package com.improvemyskills.gatewayservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReativeJwtAuthConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

	private final JwtAuthConverter jwtAuthConverter;

	public ReativeJwtAuthConverter(JwtAuthConverter jwtAuthConverter) {
		this.jwtAuthConverter = jwtAuthConverter;
	}
	@Override
	public Mono<AbstractAuthenticationToken> convert(Jwt source) {
		return Mono.just(jwtAuthConverter.convert(source));
	}
}
