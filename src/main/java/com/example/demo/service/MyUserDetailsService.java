package com.example.demo.service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {
	//ユーザー名を受け取りデータベースからユーザー情報を取得するクラス
	
    private final PersonRepository repository;

    public MyUserDetailsService(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        return new MyUserDetails(person);
    }
    
}
