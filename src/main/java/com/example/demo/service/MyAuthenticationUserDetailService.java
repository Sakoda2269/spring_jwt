package com.example.demo.service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MyAuthenticationUserDetailService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>{

	
	//送られてきたJWTからユーザーデータを取得
	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		DecodedJWT decodedJWT;
		try {
			decodedJWT = JWT.require(Algorithm.HMAC256("secret")).build().verify(token.getPrincipal().toString());
		} catch (JWTDecodeException ex) {
			
			throw new BadCredentialsException("Authorization header token is invalid");
		}
		
		if (decodedJWT.getToken().isEmpty()) {
			throw new UsernameNotFoundException("Authorization header must not be empty");
		}
		
		Person person = new Person();
		person.setUsername(decodedJWT.getClaim("username").asString());
		person.setPassword("");
		person.setId(decodedJWT.getClaim("id").asString());
		person.setRoles(decodedJWT.getClaim("role").asString());
		return new MyUserDetails(person);
		
	}
	
	

}
