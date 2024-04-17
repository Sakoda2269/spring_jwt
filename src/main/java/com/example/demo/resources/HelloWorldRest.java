package com.example.demo.resources;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.service.MyUserDetails;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
@Component
public class HelloWorldRest {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_HTML)
    public String getMessage() {
        return "message";
    }
    
    @GET
    @Path("/mydata")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyData() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if (auth != null) {
    		JSONObject json = new JSONObject();
    		Object principal = auth.getPrincipal();
    		String id = ((MyUserDetails) principal).getPerson().getId();
    		
    		Map<String, Object> map = jdbcTemplate.queryForMap("SELECT * FROM PERSON WHERE id = ?", id);
    		
    		for(Entry<String, Object> entry: map.entrySet()) {
    			json.put(entry.getKey(), entry.getValue());
    		}
    		
    		if (principal instanceof MyUserDetails) {
    			return json.toString();
    		}
    	}
    	return "auth error";
    }
    
    @GET
    @Path("/name")
    @Produces(MediaType.TEXT_PLAIN)
    public String getName() throws Exception {
    	//認証されたユーザー情報はSecurityContextHolderから取得する
    	MyUserDetails user = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return user.getUsername();
    }
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String postMessage(@FormParam("message") String m) {
    	jdbcTemplate.update("INSERT INTO MESSAGE VALUES (?, ?)", UUID.randomUUID().toString(), m);
    	return "Success!!";
    }
}