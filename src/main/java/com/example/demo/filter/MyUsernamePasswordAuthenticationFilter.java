package com.example.demo.filter;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.form.LoginForm;
import com.example.demo.service.MyUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        // "/api/login" の場合に認証を行うよう設定
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
        
        Date issuedAt = new Date();

        // 成功した場合の処理, 今回は取得した person 情報を返しています。
        this.setAuthenticationSuccessHandler((req, res, ex) -> {
        	Object principal = ex.getPrincipal();
        	String id = ((MyUserDetails) principal).getPerson().getId();
        	//ログインに成功したらJWTを生成
        	String token = JWT.create()
        			.withIssuer("test-issuer")
        			.withIssuedAt(issuedAt)
        			.withExpiresAt(new Date(issuedAt.getTime() + 1000 * 60 * 60))
        			.withClaim("username", ex.getName())
        			.withClaim("id", id)
        			.withClaim("role", ex.getAuthorities().iterator().next().toString())
        			.sign(Algorithm.HMAC256("secret"));
        	
        	res.setHeader("X-AUTH-TOKEN", token);
            res.setStatus(200);
            MyUserDetails user = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            res.getWriter().write((new ObjectMapper()).writeValueAsString(user.getPerson()));
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // リクエストのデータを LoginForm として取り出す
            LoginForm principal = new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);
            // 認証処理を実行する
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(principal.getUsername(), principal.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
