package com.improvemyskills.productservice.security;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RefreshScope
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final JwtAuthConverter jwtAuthConverter;

	public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
		this.jwtAuthConverter = jwtAuthConverter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.cors(Customizer.withDefaults())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable())
				.headers(h -> h.frameOptions(fo -> fo.disable()))
				.authorizeHttpRequests(ar -> ar.requestMatchers("/h2-console/**","/swagger-ui.html","/v3/**","/swagger-ui/**").permitAll())
				//.authorizeHttpRequests(ar -> ar.requestMatchers("/customers").hasAnyAuthority("ADMIN"))
				.authorizeHttpRequests(ar -> ar.anyRequest().authenticated())
				//.oauth2ResourceServer(o2 -> o2.jwt(Customizer.withDefaults()))
				.oauth2ResourceServer(o2 -> o2.jwt(
						jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter)
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
