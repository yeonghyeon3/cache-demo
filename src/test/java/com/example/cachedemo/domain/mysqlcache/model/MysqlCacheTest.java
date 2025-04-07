package com.example.cachedemo.domain.mysqlcache.model;

import com.example.cachedemo.AbstractMySQLContainerTest;
import com.example.cachedemo.application.MysqlCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class MysqlCacheTest extends AbstractMySQLContainerTest {

    private MysqlCacheService cacheService;  // 목 객체
    private MysqlCache mysqlCache;
    private final String cacheName = "testCache";
    private final String key = "key1";

    @BeforeEach
    public void setup() {
        // Mockito를 이용해 MysqlCacheService를 목으로 생성
        cacheService = mock(MysqlCacheService.class);
        // 테스트 대상 객체 생성
        mysqlCache = new MysqlCache(cacheName, cacheService);
    }

    @Test
    public void testPutCallsServiceAndGetReturnsCachedValue() throws IOException {
        // 테스트 값 준비
        String originalValue = "Hello, world!";
        byte[] serializedValue = serialize(originalValue);

        // put 호출 시, 내부적으로 cacheService.putCacheValue()가 호출되어야 함
        mysqlCache.put(key, originalValue);
        // 목 객체에 대해 putCacheValue가 한 번 호출되었는지 검증
        verify(cacheService, times(1)).putCacheValue(eq(cacheName), eq(key), any());

        // getCacheValue() 호출 시, 미리 직렬화한 값을 반환하도록 목 설정
        MysqlCacheValue cacheValue = new MysqlCacheValue(serializedValue);
        when(cacheService.getCacheValue(cacheName, key)).thenReturn(cacheValue);

        // get() 호출: cacheService에서 반환한 값이 역직렬화되어 originalValue와 같아야 함
        Cache.ValueWrapper wrapper = mysqlCache.get(key);
        assertThat(wrapper).isNotNull();
        Object retrievedValue = wrapper.get();
        assertThat(retrievedValue).isEqualTo(originalValue);
    }

    @Test
    public void testEvictAndClear() {
        // evict() 호출 시, cacheService.clearCacheValue()가 호출되어야 함
        mysqlCache.evict(key);
        verify(cacheService, times(1)).clearCacheValue(eq(cacheName), eq(key));

        // clear() 호출 시, cacheService.clearCacheEntries()가 호출되어야 함
        mysqlCache.clear();
        verify(cacheService, times(1)).clearCacheEntries(eq(cacheName));
    }

    // 헬퍼 메서드: Java 기본 직렬화를 사용하여 객체를 byte[]로 변환
    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }
}
