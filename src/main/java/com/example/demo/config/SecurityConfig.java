package com.example.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.filter.MyRequestHeaderAuthenticationFilter;
import com.example.demo.filter.MyUsernamePasswordAuthenticationFilter;
import com.example.demo.filter.TestFilter;
import com.example.demo.service.MyAuthenticationUserDetailService;
import com.example.demo.service.MyUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

	@Autowired
	public void configureProvider(
			//AuthenticationManagerBuiledは認証するためのユーザー情報をどこから持ってくるかを設定するためのもの
			//認証の方法をカスタマイズできる。
			AuthenticationManagerBuilder auth,
			MyUserDetailsService myUserDetailsService,
			MyAuthenticationUserDetailService myAuthenticationUserDetailService
		) throws Exception {
		//PreAuthenticatedAuthenticationPrividerは事前に認証された状態のリクエストを処理する?
		PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
		// 送られてきたJWTからユーザーを識別するためのクラス(インスタンス)を指定
		preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(myAuthenticationUserDetailService);
		//有効なアカウントかどうかチェックするためのクラス(インスタンス)を指定
		preAuthenticatedAuthenticationProvider.setUserDetailsChecker(new AccountStatusUserDetailsChecker());
		//これによりSpring Securityの認証プロセスにおいてユーザー認証のプロバイダーを構築できる
		auth.authenticationProvider(preAuthenticatedAuthenticationProvider);
		//ここまでの処理でJWTを使った認証方法を設定している
		
		
		//DaoAuthenticationProviderはデータベースの情報を使用して認証を行うプロバイダー
		//JWTを発行する前の認証の時に使うと思われる
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		//ユーザー名からデータベースのユーザーをとってくるためのクラス(インスタンス)を指定
		daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
		//パスワードのハッシュ方法を指定
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(8));
		auth.authenticationProvider(daoAuthenticationProvider);
		//ここまでの処理でJWTを発行する際のログインするときの認証方法を設定している。
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(8);
	}
	
	//AuthenticationManagerBuilderをカスタマイズした場合に必要?
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration autheticationConfiguration) throws Exception {
		return autheticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public FilterRegistrationBean<TestFilter> testFilter() {
		FilterRegistrationBean<TestFilter> bean = new FilterRegistrationBean<TestFilter>(new TestFilter());
		bean.addUrlPatterns("/api/mydata");
		bean.setOrder(1);
		return bean;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		AuthenticationManager authManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
		http
			.securityMatcher(AntPathRequestMatcher.antMatcher("/h2-console/**"))
			.authorizeHttpRequests(authz -> authz
					.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll())
			.csrf(csrf -> csrf
					.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
			.headers(headers -> headers.frameOptions(
					frame -> frame.sameOrigin()))
			
			.securityMatcher(AntPathRequestMatcher.antMatcher("/api/**"))
			.authorizeHttpRequests(authz -> authz
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/login")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/signup")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/mydata")).authenticated()
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/message")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/message/**")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).authenticated()
			)
			.headers(headers -> headers.frameOptions(
					frame -> frame.sameOrigin()))
			.sessionManagement(session -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilter(new MyUsernamePasswordAuthenticationFilter(authManager))
			.addFilter(new MyRequestHeaderAuthenticationFilter(authManager))
			.csrf(csrf -> csrf
					.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/**")))
			
			;
		return http.build();
	}
	
	
	
	
}