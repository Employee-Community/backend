package com.community.backend.common.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService<T> {

    public void setValue(String key, T value);

    public void setValue(String key, T value, Long duration);

    public void setValue(String key, T value, Long duration, TimeUnit timeUnit);

    public void incrementValue(String key, Long delta);

    public T getValue(String key);

    public Boolean hasKey(String key);

    public Set<String> getKeys(String keyPattern);

    public void deleteValue(String key);
}
