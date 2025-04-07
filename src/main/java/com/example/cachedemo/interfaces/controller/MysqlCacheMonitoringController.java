package com.example.cachedemo.interfaces.controller;

import com.example.cachedemo.application.CachePreloadService;
import com.example.cachedemo.domain.mysqlcache.MysqlCacheManager;
import com.example.cachedemo.domain.mysqlcache.model.MysqlCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.Cache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/monitor/caches")
public class MysqlCacheMonitoringController {
    private final MysqlCacheManager mysqlCacheManager;

    public MysqlCacheMonitoringController(MysqlCacheManager mysqlCacheManager, CachePreloadService cachePreloadService) {
        this.mysqlCacheManager = mysqlCacheManager;
        this.cachePreloadService = cachePreloadService;
    }

    private final CachePreloadService cachePreloadService;
    private final ObjectMapper objMapper = new ObjectMapper();

    // cache name에 해당하는 entiry 목록 조회
    @GetMapping("/{cacheName}/entries")
    public ResponseEntity<String> getCacheEntries(
            @PathVariable String cacheName
    ) throws Exception {
        Cache cache = mysqlCacheManager.getCache(cacheName);
        if (cache == null) {
            return ResponseEntity.notFound().build();
        }

        MysqlCache mysqlCache = (MysqlCache) cache;
        List<Map<String, Object>> cacheEntries = mysqlCache.getCacheEntries(cacheName);

        return ResponseEntity.ok(objMapper.writeValueAsString(cacheEntries));
    }

    @GetMapping("/cacheNames")
    public ResponseEntity<List<String>> getCacheNames() {
        List<String> cacheNames = (List<String>) mysqlCacheManager.getCacheNames();

        return ResponseEntity.ok(cacheNames);
    }

    @GetMapping("clear/{cacheName}")
    public ResponseEntity<String> clearCacheEntries(
            @PathVariable String cacheName
    ) {
        Cache cache = mysqlCacheManager.getCache(cacheName);
        if (cache == null) {
            return ResponseEntity.notFound().build();
        }

        cache.clear();

        return ResponseEntity.ok("clear cache :" + cacheName);
    }

    @GetMapping("clearAll")
    public ResponseEntity<String> clearCacheEntries() {
        mysqlCacheManager.clearAll();

        return ResponseEntity.ok("clear all");
    }

    @GetMapping("init")
    public ResponseEntity<String> cacheInit() {
        cachePreloadService.init();
        return ResponseEntity.ok("cache init");
    }
}