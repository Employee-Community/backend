package com.community.backend.common.security.jwt;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.community.backend.common.util.RedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtBlackListService {

	private final JwtTokenProvider jwtTokenProvider;
	private final RedisService<String> redisService;
	private static final String REFRESH_BLACKLIST_PREFIX = "refresh_blacklist:";

	/**
	 * refreshToken addBlackList, accessToken은 만료시간이 짧기 때문에 front에서 관리
	 */
	public void addBlackList(String token) {

		redisService.setValue(
				REFRESH_BLACKLIST_PREFIX + token,
				"logOut",
				jwtTokenProvider.getExpire(token),
				TimeUnit.MILLISECONDS);
	}

	/**
	 * Null인 경우 false를 반환하기 위해 Boolean.TRUE 사용
	 */
	public boolean isBlackList(String token) {

		return Boolean.TRUE.equals(redisService.hasKey(REFRESH_BLACKLIST_PREFIX + token));
	}
}
