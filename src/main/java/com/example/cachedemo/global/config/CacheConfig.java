package com.example.cachedemo.global.config;

import com.example.cachedemo.application.MysqlCacheService;
import com.example.cachedemo.domain.mysqlcache.MysqlCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class CacheConfig {

    @Primary
    @Bean
    public MysqlCacheManager cacheManager(MysqlCacheService mysqlCacheService) {
        return new MysqlCacheManager(mysqlCacheService);
    }
}


