package com.example.cachedemo.infrastructure.persistence.mybatis;

import com.example.cachedemo.AbstractMySQLContainerTest;
import com.example.cachedemo.domain.mysqlcache.model.MysqlCacheValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class MybatisMysqlCacheRepositoryTest extends AbstractMySQLContainerTest {


    // MybatisMysqlCacheRepository는 MysqlCacheRepository를 확장한 인터페이스입니다.
    @Autowired
    private MybatisMysqlCacheRepository repository;

    // 테스트 전에 특정 캐시 이름의 데이터를 클리어합니다.
    @BeforeEach
    public void setup() {
        // 전체 캐시 삭제: 모든 캐시 이름에 대한 데이터를 삭제합니다.
        repository.deleteCacheAllEntries();
    }

    @Test
    @DisplayName("Mybatis 캐시 등록 테스트")
    public void testInsertAndSelectCacheValue() {
        // given
        String cacheName = "testCache";
        String key = "testKey";
        byte[] value = "testValue".getBytes(StandardCharsets.UTF_8);

        // when
        repository.insertCacheValue(cacheName, key, value);

        // then
        MysqlCacheValue cacheValue = repository.selectCacheValue(cacheName, key);
        assertThat(cacheValue).isNotNull();
        String storedValue = new String(cacheValue.getData(), StandardCharsets.UTF_8);
        assertThat(storedValue).isEqualTo("testValue");
    }

    @Test
    @DisplayName("캐시 이름에 해당하는 모든 Entry의 개수를 조회한다.")
    public void testSelectCacheEntriesAndCount() {
        String cacheName = "testCache";

        // 여러 엔트리 삽입
        repository.insertCacheValue(cacheName, "key1", "value1".getBytes(StandardCharsets.UTF_8));
        repository.insertCacheValue(cacheName, "key2", "value2".getBytes(StandardCharsets.UTF_8));

        // 특정 캐시 이름으로 모든 엔트리 조회
        List<Map<String, Object>> entries = repository.selectCacheEntries(cacheName);
        int count = repository.selectCacheEntriesCount(cacheName);

        // 엔트리 수가 두 건이어야 함
        assertThat(entries).isNotNull();
        assertThat(entries.size()).isEqualTo(2);
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("캐시 이름과 캐시 키에 해당하는 캐시 값을 삭제한다.")
    public void testDeleteCacheValue() {
        String cacheName = "testCache";
        String key = "testKeyToDelete";
        byte[] value = "toDelete".getBytes(StandardCharsets.UTF_8);

        repository.insertCacheValue(cacheName, key, value);
        MysqlCacheValue cacheValue = repository.selectCacheValue(cacheName, key);
        assertThat(cacheValue).isNotNull();

        // 특정 캐시 키 삭제
        repository.deleteCacheValue(cacheName, key);
        MysqlCacheValue deletedValue = repository.selectCacheValue(cacheName, key);
        assertThat(deletedValue).isNull();
    }

    @Test
    @DisplayName("모든 캐시를 삭제한다.")
    public void testDeleteCacheAllEntries() {
        String cacheName1 = "cache1";
        String cacheName2 = "cache2";

        repository.insertCacheValue(cacheName1, "key1", "value1".getBytes(StandardCharsets.UTF_8));
        repository.insertCacheValue(cacheName2, "key2", "value2".getBytes(StandardCharsets.UTF_8));

        // 모든 캐시 삭제 (DB의 모든 캐시 엔트리 삭제)
        repository.deleteCacheAllEntries();

        MysqlCacheValue value1 = repository.selectCacheValue(cacheName1, "key1");
        MysqlCacheValue value2 = repository.selectCacheValue(cacheName2, "key2");
        assertThat(value1).isNull();
        assertThat(value2).isNull();
    }
}
