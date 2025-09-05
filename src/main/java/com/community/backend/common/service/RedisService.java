package com.community.backend.common.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService<T> {

    void setValue(String key, T value);

    void setValue(String key, T value, Long duration);

    void setValue(String key, T value, Long duration, TimeUnit timeUnit);

    void incrementValue(String key, Long delta);

    T getValue(String key);

    Boolean hasKey(String key);

    Set<String> getKeys(String keyPattern);

    void deleteValue(String key);
}
