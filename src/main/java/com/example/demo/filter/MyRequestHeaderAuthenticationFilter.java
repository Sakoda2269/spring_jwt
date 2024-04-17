package com.example.demo.filter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {
	
	public MyRequestHeaderAuthenticationFilter(AuthenticationManager authenticationManager) {
		//ヘッダーのAuthorizationの情報を用いて認証を行う
		setPrincipalRequestHeader("Authorization");
		//ヘッダーにAuthorizationがない場合に例外を投げるかどうか
		setExceptionIfHeaderMissing(false);
		
		//?認証に必要?
		setAuthenticationManager(authenticationManager);
		
		// /api/**のパスに来たらtokenによる認証を行う。SecurityConfigで設定している場合必要ない？
		//setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/**"));
		
		//認証に成功した場合の処理
		this.setAuthenticationSuccessHandler((req, res, ex) -> {
			log.info("Success 2");
		});
		//認証に失敗した場合の処理
		this.setAuthenticationFailureHandler((req, res, ex) -> {
			res.setStatus(403);
			log.info("Failure 2");
		});
	}

}
