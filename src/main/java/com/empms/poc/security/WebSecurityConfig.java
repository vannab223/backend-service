package com.empms.poc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.empms.poc.security.jwt.AuthEntryPointJwt;
import com.empms.poc.security.jwt.AuthTokenFilter;
import com.empms.poc.security.services.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
	 * Exception { http.csrf(csrf -> csrf.disable()) .exceptionHandling(exception ->
	 * exception.authenticationEntryPoint(unauthorizedHandler))
	 * .sessionManagement(session ->
	 * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	 * .authorizeHttpRequests( auth -> auth.requestMatchers("/api/auth/**",
	 * "/swagger-ui/index.html", "/swagger-ui.html", "/swagger", "/swagger-ui",
	 * "favicon.ico", "/favicon.ico", "/swagger-ui/*", "swagger-ui/index.html",
	 * "/swagger-ui/index.html/").permitAll().anyRequest().authenticated());
	 * 
	 * http.authenticationProvider(authenticationProvider());
	 * 
	 * http.addFilterBefore(authenticationJwtTokenFilter(),
	 * UsernamePasswordAuthenticationFilter.class);
	 * 
	 * return http.build(); }
	 */
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/api/auth/**",
	                "/swagger-ui/**",
	                "/swagger-ui.html",
	                "/v3/api-docs/**",
	                "/v3/api-docs",
	                "/swagger-resources/**",
	                "/configuration/ui",
	                "/configuration/security",
	                "/webjars/**",
	                "/favicon.ico"
	            ).permitAll()
	            .anyRequest().authenticated()
	        );

	    http.authenticationProvider(authenticationProvider());
	    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}


}
