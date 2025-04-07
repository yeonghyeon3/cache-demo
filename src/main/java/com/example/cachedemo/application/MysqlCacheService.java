package com.example.cachedemo.application;

import com.example.cachedemo.domain.mysqlcache.repository.MysqlCacheRepository;
import com.example.cachedemo.domain.mysqlcache.model.MysqlCacheValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MysqlCacheService {
    public MysqlCacheService(MysqlCacheRepository mysqlCacheRepository) {
        this.mysqlCacheRepository = mysqlCacheRepository;
    }

    private final MysqlCacheRepository mysqlCacheRepository;

    @Transactional(readOnly = true)
    public MysqlCacheValue getCacheValue(String name, String key) {
        return mysqlCacheRepository.selectCacheValue(name, key);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCacheEntries(String name) {
        return mysqlCacheRepository.selectCacheEntries(name);
    }

    @Transactional
    public void putCacheValue(String name, String key, byte[] value) {
        mysqlCacheRepository.insertCacheValue(name, key, value);
    }

    @Transactional
    public void clearCacheValue(String name, String key) {
        mysqlCacheRepository.deleteCacheValue(name, key);
    }

    @Transactional
    public void clearCacheEntries(String name) {
        mysqlCacheRepository.deleteCacheEntries(name);
    }

    @Transactional(readOnly = true)
    public int getCacheSize(String name) {
        return mysqlCacheRepository.selectCacheEntriesCount(name);
    }

    @Transactional
    public void clearAll() {
        mysqlCacheRepository.deleteCacheAllEntries();
    }
}
