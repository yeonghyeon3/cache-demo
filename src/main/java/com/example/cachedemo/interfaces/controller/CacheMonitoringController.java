package com.example.cachedemo.interfaces.controller;

import com.example.cachedemo.interfaces.dto.CacheStatsDTO;
import com.example.cachedemo.interfaces.dto.CaffeineCacheStatsDTO;
import com.example.cachedemo.interfaces.dto.RedisCacheStatsDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
public class CacheMonitoringController {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * 지정한 캐시 이름의 Caffeine 통계 정보를 반환합니다.
     *
     * 예시 요청: GET /cache/stats/dashboardData
     */
    @GetMapping("/stats/{cacheName}")
    public ResponseEntity<CacheStatsDTO> getCacheStats(@PathVariable String cacheName) {
        org.springframework.cache.Cache springCache = cacheManager.getCache(cacheName);
        if (springCache == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cache not found: " + cacheName);
        }
        if (springCache instanceof CaffeineCache) {
            CaffeineCache caffeineCache = (CaffeineCache) springCache;
            Cache nativeCache = caffeineCache.getNativeCache();

            Set<String> keys = nativeCache.asMap().keySet();
            CacheStats stats = nativeCache.stats();

            CaffeineCacheStatsDTO.Builder builder = CaffeineCacheStatsDTO.builder(cacheName, stats);
            for (String key : keys) {
                builder.addKey(key);
            }
            CacheStatsDTO response = builder.build();
            return ResponseEntity.ok(response);
        }
        else if (springCache instanceof RedisCache) {
            RedisCache redisCache = (RedisCache) springCache;
            // RedisCache 기본 키 패턴은 보통 "cacheName::" 형태입니다.
            String pattern = redisCache.getName() + "::*";
            Set<String> keys = redisTemplate.keys(pattern);

            RedisCacheStatsDTO.Builder builder = RedisCacheStatsDTO.builder(cacheName, pattern);
            for (String key : keys) {
                builder.addKey(key);
            }
            CacheStatsDTO response = builder.build();
            return ResponseEntity.ok(response);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cache is not backed by Caffeine");
        }
    }
}
