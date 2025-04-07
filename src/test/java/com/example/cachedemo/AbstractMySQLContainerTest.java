package com.example.cachedemo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractMySQLContainerTest {

    // MySQL 컨테이너를 static 필드로 선언하여 모든 테스트 클래스에서 공유
    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER =
            new MySQLContainer<>("mysql:8.0.32")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @DynamicPropertySource
    static void overrideDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL_CONTAINER::getDriverClassName);
    }

    // (선택사항) 컨테이너 정리 로직은 Testcontainers가 자동으로 처리함
    @BeforeAll
    static void setUp() {
        // 컨테이너 시작 여부를 로그로 확인하거나 초기화 작업 수행
        System.out.println("Starting MySQL container...");
    }

    @AfterAll
    static void tearDown() {
        // 컨테이너 종료 여부를 로그로 확인하거나 추가 작업 수행
        System.out.println("MySQL container will be stopped automatically.");
    }
}
