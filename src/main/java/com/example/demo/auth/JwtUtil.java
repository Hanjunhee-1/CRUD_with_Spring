package com.example.demo.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.users.domain.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final SecretKey secretKey;
	private final Long validityMilliSeconds;
	
	// jwt 생성자. secret 과 expiration 을 설정해줌.
	public JwtUtil(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration}") Long validityMilliSeconds
		) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.validityMilliSeconds = validityMilliSeconds;
	}
	
	/**
	 * 
	 * @param user
	 * 
	 * 액세스 토큰 생성
	 * 
	 */
	public String createAccessToken(Users user) {
		return createToken(user, validityMilliSeconds);
	}
	
	/**
	 * 
	 * @param user
	 * @param validityMilliSeconds
	 * 
	 * JWT 토큰 생성. private
	 * 
	 */
	private String createToken(Users user, Long validityMilliSeconds) {
		final Date now = new Date();
        final Date expiredDate = new Date(now.getTime() + validityMilliSeconds);
        
        return Jwts.builder()
        		.claim("userId", user.getId())
        		.claim("nickname", user.getNickname())
        		.issuedAt(now)
        		.expiration(expiredDate)
        		.signWith(secretKey)
        		.compact();
	}
	
	/**
	 * 
	 * @param bearerToken
	 *  
	 * Authorization 헤더에서 Bearer 제거
	 */
	public String resolveToken(String bearerToken) {
		if (bearerToken == null) return null;
		if (!bearerToken.startsWith("Bearer ")) return null;
		return bearerToken.substring(7);
	}
	
	/**
	 * 
	 * @param token
	 * 
	 * 토큰 정보 추출.
	 */
	public Jws<Claims> parseToken(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);
	}
	
	/**
	 * 
	 * @param token
	 * 
	 * 토큰 페이로드 추출. private
	 */
	private Claims extractPayload(String token) {
		return parseToken(token).getPayload();
	}
	
	/**
	 * 
	 * @param token
	 * 
	 * userId 추출.
	 */
	public Long extractUserId(String token) {
		return extractPayload(token).get("userId", Long.class);
	}
	
	/**
	 * 
	 * @param token
	 * 
	 * nickname 추출.
	 */
	public String extractNickname(String token) {
		return extractPayload(token).get("nickname", String.class);
	}
	
	/**
	 * 
	 * @param token
	 * 
	 * 만료시간 추출. UTC 기준
	 */
	public Date extractExpiration(String token) {
		return extractPayload(token).getExpiration();
	}
	
	/**
	 * 
	 * @param token
	 * 
	 * 토큰 만료 여부 확인
	 */
	public boolean isExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}
