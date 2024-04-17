package com.example.demo.filter;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.service.MyUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

//UsernamePasswordAuthenticationFiletrはユーザーネームとパスワードを使って認証するためのフィルター
//JWTを生成するときに使う。
@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    
    //loginの処理を行い、JWTを返すフィルター。
    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager; //コンストラクタインジェクション?

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
        	
        	//ヘッダーの"X-AUTH-TOKEN"にtokenをつける
        	res.setHeader("X-AUTH-TOKEN", token);
            res.setStatus(200);
            
            //ついでにユーザー情報もJOSNとしてBodyに入れて返す
//            MyUserDetails user = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            res.getWriter().write((new ObjectMapper()).writeValueAsString(user.getPerson()));
        });
    }

    //認証を実施する
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    	log.info(request.getParameter("username"));
    	
    	//formparameterの"username"と"password"を使って認証をする
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
        		//new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);
        // 認証処理を実行する
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }
}
