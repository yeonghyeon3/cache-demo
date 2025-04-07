package com.example.cachedemo.domain.mysqlcache;


import com.example.cachedemo.application.MysqlCacheService;
import com.example.cachedemo.domain.mysqlcache.model.MysqlCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlCacheManager implements CacheManager {
    private final MysqlCacheService mysqlCacheService;
    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    public MysqlCacheManager(
            MysqlCacheService mysqlCacheService
    ) {
        this.mysqlCacheService = mysqlCacheService;
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name, n -> new MysqlCache(n, mysqlCacheService));
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }

    public void clearAll() {
        mysqlCacheService.clearAll();
    }
}
