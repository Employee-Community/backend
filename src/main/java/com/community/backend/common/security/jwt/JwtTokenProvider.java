package com.community.backend.common.security.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

	private static final String BEARER_PREFIX = "Bearer ";
	public static final long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000L;
	public static final long REFRESH_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L;

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {

		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createToken(Long idx, String email, String nickname, Long expire) {

		return Jwts.builder()
			.setSubject(String.valueOf(idx))
			.claim("email", email)
			.claim("nickname", nickname)
			.setExpiration(new Date(System.currentTimeMillis() + expire))
			.setIssuedAt(new Date())
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	public String extractToken(String authorizationHeader) {

		if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
			return authorizationHeader.substring(BEARER_PREFIX.length());
		}

		throw new AuthenticationCredentialsNotFoundException("토큰을 찾을 수 없습니다.");
	}

	public Claims parseToken(String token) {

		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public Long getSubject(String token) {

		return Long.parseLong(parseToken(token).getSubject());
	}

	public String getEmail(String token) {

		return parseToken(token).get("email", String.class);
	}

	public String getNickname(String token) {

		return parseToken(token).get("nickname", String.class);
	}

	public Long getExpire(String token) {

		return parseToken(token).getExpiration().getTime() - System.currentTimeMillis();
	}

	public ResponseCookie createRefreshTokenCookie(String refresh) {

		return ResponseCookie.from("refresh", refresh)
			.httpOnly(true)
			.maxAge(REFRESH_TOKEN_EXPIRATION_TIME)
			.path("/")
			.build();
	}
}
