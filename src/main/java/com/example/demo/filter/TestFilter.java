package com.example.demo.filter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestFilter extends OncePerRequestFilter{
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader("Authorization");
		DecodedJWT decodedJWT = null;
		try {
			decodedJWT = JWT.require(Algorithm.HMAC256("secret")).build().verify(token);
		} catch (JWTDecodeException ex) {
			
		}
		
		if(decodedJWT != null) {
			List<String> tmp = Arrays.asList(decodedJWT.getClaim("role").asString().split(","));
			for(String s : tmp) {
				log.info(s);
			}
//			if(!tmp.contains("ROLE_ADMIN")) {
//				response.sendError(HttpServletResponse.SC_FORBIDDEN);
//			}
		}
		filterChain.doFilter(request, response);
	}

}
