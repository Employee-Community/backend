package com.community.backend.common.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import lombok.RequiredArgsConstructor;

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
        try {
            T value = redisTemplate.opsForValue().get(key);

            // 값이 숫자형인 경우에만 증가
            if (value != null && value instanceof Number) {
                redisTemplate.opsForValue().increment(key, delta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public T getValue(String key) {
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
