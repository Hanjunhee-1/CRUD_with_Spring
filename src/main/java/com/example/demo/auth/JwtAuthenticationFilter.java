package com.example.demo.auth;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.users.domain.Users;
import com.example.demo.users.repository.UsersRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 요청이 들어올 때마다 JWT를 검사해서 로그인 상태를 만들어주는 역할
// jwt 방식은 session 과 다르게 서버가 로그인 상태를 검사하는 것이 아니기 때문에 (stateless 방식) filter 가 해당 역할을 해줌.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final UsersRepository usersRepository;
	
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UsersRepository usersRepository) {
        this.jwtUtil = jwtUtil;
        this.usersRepository = usersRepository;
    }
    
    @Override
    protected void doFilterInternal (
    		HttpServletRequest request,
    		HttpServletResponse response,
    		FilterChain filterChain
    	) throws ServletException, IOException {
    	
    	String authorizationHeader = request.getHeader("Authorization");
    	String token = jwtUtil.resolveToken(authorizationHeader);
    	
    	// 토큰이 없다면 그냥 통과.
    	if (token == null) {
    		filterChain.doFilter(request, response);
    		return ;
    	}
    	
    	// 토큰이 유효하지 않다면 그냥 통과.
    	if (!jwtUtil.validateToken(token)) {
    		filterChain.doFilter(request, response);
    		return ;
    	}
    	
    	Long userId = jwtUtil.extractUserId(token);
    	Users user = usersRepository.findById(userId).orElse(null);
    	
    	if (user != null) {
    		// Spring Security 인증 객체 생성 (권한은 따로 없으므로 null)
    		UsernamePasswordAuthenticationToken authentication = 
    				new UsernamePasswordAuthenticationToken(user, null, List.of());
    		
    		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    		
    		// security context 에 등록.
    		SecurityContextHolder.getContext().setAuthentication(authentication);
    	}
    	
    	filterChain.doFilter(request, response);
    }
}
