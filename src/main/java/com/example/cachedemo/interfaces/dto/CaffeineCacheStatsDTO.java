package com.example.cachedemo.interfaces.dto;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CaffeineCacheStatsDTO implements CacheStatsDTO{
    private final String cacheType = "Caffeine";

    private final String cacheName;
    private final long hitCount;
    private final long missCount;
    private final long loadSuccessCount;
    private final long loadFailureCount;
    private final double totalLoadTime;
    private final long evictionCount;
    private final long evictionWeight;
    private final int size;
    private final Set<String> keys;


    public CaffeineCacheStatsDTO(Builder builder) {
        this.cacheName = builder.cacheName;
        this.hitCount = builder.hitCount;
        this.missCount = builder.missCount;
        this.loadSuccessCount = builder.loadSuccessCount;
        this.loadFailureCount = builder.loadFailureCount;
        this.totalLoadTime = builder.totalLoadTime;
        this.evictionCount = builder.evictionCount;
        this.evictionWeight = builder.evictionWeight;
        this.keys = builder.keys;
        this.size = builder.keys.size();
    }

    public static Builder builder(String cacheName, CacheStats stats) {
        return new Builder(cacheName, stats);
    }

    public static class Builder {
        private String cacheName;
        private long hitCount;
        private long missCount;
        private long loadSuccessCount;
        private long loadFailureCount;
        private double totalLoadTime;
        private long evictionCount;
        private long evictionWeight;

        private Set<String> keys;

        private Builder(String cacheName, CacheStats stats) {
            this.cacheName = cacheName;
            this.hitCount = stats.hitCount();
            this.missCount = stats.missCount();
            this.loadSuccessCount = stats.loadSuccessCount();
            this.loadFailureCount = stats.loadFailureCount();
            this.totalLoadTime = stats.totalLoadTime();
            this.evictionCount = stats.evictionCount();
            this.evictionWeight = stats.evictionWeight();
            this.keys = new HashSet<>();
        }

        public Builder addKey(String key) {
            this.keys.add(key);
            return this;
        }

        public CaffeineCacheStatsDTO build() {
            return new CaffeineCacheStatsDTO(this);
        }
    }
}
