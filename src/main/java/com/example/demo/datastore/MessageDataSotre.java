package com.example.demo.datastore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public class MessageDataSotre implements DataDict<String>{
	
	private HashMap<String, String> messages;
	
	public MessageDataSotre() {
		messages  = new HashMap<>();
	}
	
	@Override
	public void put(String key, String data) {
		this.messages.put(key, data);
	}

	@Override
	public String get(String key) {
		return messages.get(key);
	}

	@Override
	public void delete(String key) {
		messages.remove(key);
	}

	@Override
	public Iterator<Entry<String, String>> iterator() {
		
		return messages.entrySet().iterator();
	}

}
