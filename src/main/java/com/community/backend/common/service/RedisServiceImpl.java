package com.community.backend.common.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisServiceImpl<T> implements RedisService<T> {

    private final RedisTemplate<String, T> redisTemplate;

    public static final Long LONG_TERM = 60 * 60 * 24L; // 24시간
    public static final Long SHORT_TERM = 60 * 60L; // 1시간

    @Override
    public void setValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value, SHORT_TERM, TimeUnit.SECONDS);
    }

    @Override
    public void setValue(String key, T value, Long duration) {
        redisTemplate.opsForValue().set(key, value, duration, TimeUnit.SECONDS);
    }

    @Override
    public void setValue(String key, T value, Long duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, duration, timeUnit);
    }

    @Override
    public void incrementValue(String key, Long delta) {
        redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public T getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Set<String> getKeys(String keyPattern) {
        return redisTemplate.keys(keyPattern);
    }

    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

}
