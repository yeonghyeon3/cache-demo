package com.example.cachedemo.domain.mysqlcache.model;

import com.example.cachedemo.application.MysqlCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class MysqlCache implements Cache {
    private final String name;
    private final MysqlCacheService mysqlCacheService;

    public MysqlCache(String name, MysqlCacheService mysqlCacheService) {
        this.name = name;
        this.mysqlCacheService = mysqlCacheService;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return mysqlCacheService;
    }

    @Override
    public ValueWrapper get(Object key) {
        try {
            MysqlCacheValue cacheValue = mysqlCacheService.getCacheValue(name, key.toString());
            if (cacheValue != null) {
                if (!cacheValue.isEmpty()) {
                    byte[] bytes = cacheValue.getData();
                    Object value = deserialize(bytes);
                    log.info("Cache hit in '{}': Retrieved value for key '{}'", name, key);
                    return new SimpleValueWrapper(value);
                }
            }
        } catch (Exception e) {
            log.error("Cache get error in '{}' for key '{}'", name, key, e);
        }
        log.info("Cache miss in '{}': No value found for key '{}'", name, key);
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = get(key);
        if (wrapper != null && type.isInstance(wrapper.get())) {
            return type.cast(wrapper.get());
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        throw new UnsupportedOperationException("get(key, Callable) not implemented");
    }

    @Override
    public void put(Object key, Object value) {
        byte[] bytes = serialize(value);
        if (bytes == null) return;
        try {
            mysqlCacheService.putCacheValue(name, key.toString(), bytes);
            log.info("Cache put in '{}': New value cached for key '{}'", name, key);
        } catch (Exception e) {
            log.error("Cache put in '{}': DB Insert error'", name, e);
        }
    }

    @Override
    public void evict(Object key) {
        mysqlCacheService.clearCacheValue(name, key.toString());
        log.info("Cache evict in '{}': Removed value for key '{}'", name, key);
    }

    @Override
    public void clear() {
        mysqlCacheService.clearCacheEntries(name);
        log.info("Cache clear in '{}': All entries removed", name);
    }

    public List<Map<String, Object>> getCacheEntries(String name) {
        return mysqlCacheService.getCacheEntries(name);
    }

    public int getSize() {
        return mysqlCacheService.getCacheSize(name);
    }


    private byte[] serialize(Object obj) {
        if (obj == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Cache serialization error in '{}'", name, e);
            return null;
        }
    }

    private Object deserialize(byte[] bytes) {
        if (bytes == null) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Cache deserialization error in '{}'", name, e);
            return null;
        }
    }
}
