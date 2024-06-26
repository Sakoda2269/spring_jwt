package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Person;

public interface PersonRepository extends JpaRepository<Person, String> {
	
	//指定されたusernameを持つPersonを返す
    public Optional<Person> findByUsername(String username);
    
    
    // findBy<データ名>でデータ検索ができる
    public Optional<Person> findByEmail(String email);
    
}