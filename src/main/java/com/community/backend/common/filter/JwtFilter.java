package com.community.backend.common.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.community.backend.common.security.jwt.JwtPayload;
import com.community.backend.common.security.jwt.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if(header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = jwtTokenProvider.extractToken(header);

		try{
			Claims claims = jwtTokenProvider.parseToken(token);
			if (claims == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			Long idx = jwtTokenProvider.getSubject(token);
			String email = jwtTokenProvider.getEmail(token);
			String nickname = jwtTokenProvider.getNickname(token);

			JwtPayload jwtPayload = new JwtPayload(idx, email, nickname);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtPayload, null,
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			filterChain.doFilter(request, response);
		} catch (SecurityException | ExpiredJwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
	}
}
