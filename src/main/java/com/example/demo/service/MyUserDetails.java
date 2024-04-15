package com.example.demo.service;
import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.demo.entity.Person;

import lombok.Getter;


@Getter
public class MyUserDetails extends User {

    private final Person person;
    
    //Personからusername, password, roleを受け取るクラス
    
    public MyUserDetails(Person person) {
        super(person.getUsername(),
                person.getPassword(),
                Arrays.asList(person.getRoles().split(",")).stream().map(
                        role -> new SimpleGrantedAuthority((role))).toList());
        this.person = person;
    }
}