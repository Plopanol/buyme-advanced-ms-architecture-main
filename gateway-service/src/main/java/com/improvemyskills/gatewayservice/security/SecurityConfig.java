package com.improvemyskills.gatewayservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {

		return httpSecurity
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.headers(h -> h.frameOptions(fo -> fo.disable()))
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
						.pathMatchers("/h2-console/**","/swagger-ui.html","/v3/**","/swagger-ui/**").permitAll()
						.anyExchange().authenticated()
				)
				.oauth2ResourceServer(o2 -> o2.jwt(
						jwtSpec -> jwtSpec.jwtAuthenticationConverter(
								new ReactiveJwtAuthConverterAdapter(
										new JwtAuthConverter()
								))
				))
				.build();
	}

	//    @Bean
	//    CorsConfigurationSource corsConfigurationSource() {
	//        CorsConfiguration configuration = new CorsConfiguration();
	//        configuration.setAllowedOrigins(Arrays.asList("*"));
	//        configuration.setAllowedMethods(Arrays.asList("*"));
	//        configuration.setAllowedHeaders(Arrays.asList("*"));
	//        configuration.setExposedHeaders(Arrays.asList("*"));
	//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	//        source.registerCorsConfiguration("/**", configuration);
	//        return source;
	//    }
}
