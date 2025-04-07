package com.example.cachedemo.domain.mysqlcache.repository;


import com.example.cachedemo.domain.mysqlcache.model.MysqlCacheValue;

import java.util.List;
import java.util.Map;


public interface MysqlCacheRepository {
    // Cache Value 가져오기
    public MysqlCacheValue selectCacheValue(String name, String key);

    // Cache value 등록하기
    public void insertCacheValue(String name, String key, byte[] value);

    // Cache name으로 모든 Entry 찾기
    public List<Map<String, Object>> selectCacheEntries(String name);

    // Cache name으로 모든 Entry 삭제
    public void deleteCacheEntries(String name);

    // 모든 Cache 삭제
    public void deleteCacheAllEntries();

    // key에 해당하는 캐시 삭제
    void deleteCacheValue(String name, String key);

    // Cache name의 Entry 크기 조회
    int selectCacheEntriesCount(String name);
}