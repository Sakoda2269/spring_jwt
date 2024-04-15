package com.example.demo.resources;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
@Component
public class SignupAPI {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//@Valid SignupForm form, 
	
	@POST
	@Path("/signup")
    @Produces(MediaType.TEXT_PLAIN)
    public String postMessage(@FormParam("username") String username, @FormParam("password") String password,
    		@FormParam("email") String email) {
		if(username != null && password != null && email != null) {
			String sql = "INSERT INTO person VALUES (?, ?, ?, ?, ?, ?)";
			String id = UUID.randomUUID().toString();
			jdbcTemplate.update(sql, id, username, passwordEncoder.encode(password), email, "true", "ROLE_GENERAL");
	    	return "Success!!";
		} else {
			return "validation error";
		}
		
    }
	
	
}
