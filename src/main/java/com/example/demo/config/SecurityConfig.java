package com.example.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.service.MyAuthenticationUserDetailService;
import com.example.demo.service.MyUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

	@Autowired
	public void configureProvider(
			AuthenticationManagerBuilder auth,
			MyUserDetailsService myUserDetailsService,
			MyAuthenticationUserDetailService myAuthenticationUserDetailService
		) throws Exception {
		PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
		preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(myAuthenticationUserDetailService);
		preAuthenticatedAuthenticationProvider.setUserDetailsChecker(new AccountStatusUserDetailsChecker());
		auth.authenticationProvider(preAuthenticatedAuthenticationProvider);
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(8));
		auth.authenticationProvider(daoAuthenticationProvider);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(8);
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration autheticationConfiguration) throws Exception {
		return autheticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		AuthenticationManager authManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
		http
			.securityMatcher(AntPathRequestMatcher.antMatcher("/h2-console/**"))
			.authorizeHttpRequests(authz -> authz
					.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll())
			.csrf(csrf -> csrf
					.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
			.headers(headers -> headers.frameOptions(
					frame -> frame.sameOrigin()))
			
			.securityMatcher(AntPathRequestMatcher.antMatcher("/api/**"))
			.authorizeHttpRequests(authz -> authz
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/login")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/signup")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/mydata")).authenticated()
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).hasRole("ADMIN")
			)
			.headers(headers -> headers.frameOptions(
					frame -> frame.sameOrigin()))
			.sessionManagement(session -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilter(new MyUsernamePasswordAuthenticationFilter(authManager))
			.addFilter(new MyRequestHeaderAuthenticationFilter(authManager))
			.csrf(csrf -> csrf
					.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/**")))
			
			;
		return http.build();
	}
}