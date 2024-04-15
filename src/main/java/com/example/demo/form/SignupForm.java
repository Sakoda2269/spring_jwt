package com.example.demo.form;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupForm {
	
	@NotNull
	private String username;
	
	@NotNull
	private String password;
}
