package com.example.cachedemo.interfaces.dto;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class RedisCacheStatsDTO implements CacheStatsDTO {
    private final String cacheType = "Redis";

    private String cacheName;
    private String pattern;
    private Set<String> keys;
    private Integer size;

    private RedisCacheStatsDTO(Builder builder) {
        this.cacheName = builder.cacheName;
        this.pattern = builder.pattern;
        this.keys = builder.keys;
        this.size = keys.size();
    }

    public static Builder builder(String cacheName, String pattern) {
        return new Builder(cacheName, pattern);
    }

    public static class Builder {
        private final String cacheName;
        private final String pattern;
        private Set<String> keys;

        private Builder(String cacheName, String pattern) {
            this.cacheName = cacheName;
            this.pattern = pattern;
            this.keys = new HashSet<>();
        }

        public Builder addKey(String key) {
            this.keys.add(key);
            return this;
        }

        public RedisCacheStatsDTO build() {
            return new RedisCacheStatsDTO((this));
        }
    }
}
