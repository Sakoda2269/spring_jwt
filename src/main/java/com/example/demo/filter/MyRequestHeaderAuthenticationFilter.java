package com.example.demo.filter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {
	
	public MyRequestHeaderAuthenticationFilter(AuthenticationManager authenticationManager) {
		setPrincipalRequestHeader("Authorization");
		setExceptionIfHeaderMissing(false);
		setAuthenticationManager(authenticationManager);
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/**"));
		
		this.setAuthenticationSuccessHandler((req, res, ex) -> {
			log.info("Success 2");
		});
		this.setAuthenticationFailureHandler((req, res, ex) -> {
			log.info("Failure 2");
		});
	}

}
