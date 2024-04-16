package com.example.demo.datastore;

import java.util.Map.Entry;

public interface DataDict<T> extends Iterable<Entry<String, T>>{
	
	public void put(String key, T data);
	
	public T get(String key);
	
	public void delete(String key);

}
