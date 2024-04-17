package com.example.demo.resources;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.example.demo.datastore.DataDict;
import com.example.demo.datastore.MessageDataSotre;
import com.example.demo.repository.PersonRepository;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/message")
@Component
public class MessageRest {
	
	final DataDict<String> datas;
	final PersonRepository repo;
	
	public MessageRest(MessageDataSotre store, PersonRepository repo) {
		datas = store;
		this.repo = repo;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMessages() {
		JSONObject res = new JSONObject();
		for(Entry<String, String> e : datas) {
			res.put(e.getKey(), e.getValue());
		}
		return res.toString();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addMessage(@FormParam("message") String message) {
		datas.put(UUID.randomUUID().toString(), message);
	}
}
