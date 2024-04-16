package com.example.demo.resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.datastore.DataDict;
import com.example.demo.datastore.MessageDataSotre;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/message")
@Component
public class MessageRest2 {
	
	final DataDict<String> datas;
	
	@Autowired
	public MessageRest2(MessageDataSotre store) {
		datas = store;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMessages(@PathParam("id") String id) {
		return datas.get(id);
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addMessage(@PathParam("id") String id, @FormParam("message") String message) {
		datas.put(id, message);
	}
}
