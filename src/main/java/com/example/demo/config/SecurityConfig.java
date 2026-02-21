package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.auth.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	// jwt filter
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    // filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // csrf 비활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // session 비활성화
            .formLogin(form -> form.disable()) // 로그인 페이지 비활성화
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth
            	// auth 에서는 모두 허용.
            	.requestMatchers("/auth/**").permitAll()
            	
            	// getAllUsers(), createUser() 는 모두 허용.
            	// updateUser(), deleteUser(), getMe() 는 jwt 필요.
            	.requestMatchers(HttpMethod.GET, "/users").permitAll()
            	.requestMatchers(HttpMethod.POST, "/users").permitAll()
            	.requestMatchers(HttpMethod.PATCH, "/users").authenticated()
            	.requestMatchers(HttpMethod.DELETE, "/users").authenticated()
            	.requestMatchers(HttpMethod.GET, "/boards/**").permitAll()
            	.requestMatchers(HttpMethod.POST, "/boards").authenticated()
            	.requestMatchers(HttpMethod.PATCH, "/boards/**").authenticated()
            	.requestMatchers(HttpMethod.DELETE, "/boards/**").authenticated()
            	.requestMatchers("/users/me").authenticated()
            	
            	// Swagger 허용
            	.requestMatchers(
            			"/swagger-ui/**",
            			"/v3/api-docs/**"
            	).permitAll()
            	
            	
            	// 그 외 다른 것들은 모두 허용.
                .anyRequest().permitAll()
            )
            // UsernamePasswordAuthenticationFilter 앞에 JWT 필터를 넣음
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 비밀번호 암호화
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
