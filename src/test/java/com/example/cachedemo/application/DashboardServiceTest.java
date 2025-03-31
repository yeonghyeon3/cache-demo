package com.example.cachedemo.application;

import com.example.cachedemo.domain.dashboard.model.Dashboard;
import com.example.cachedemo.domain.dashboard.repository.DashboardRepository;
import com.example.cachedemo.interfaces.dto.FetchDashboardRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {DashboardServiceTest.TestConfig.class})
public class DashboardServiceTest {

    @Configuration
    @EnableCaching
    static class TestConfig {
        // CacheManager 빈 등록 (간단한 ConcurrentMapCacheManager 사용)
        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("dashboardData");
        }

        // DashboardRepository를 Mockito.mock()으로 생성하여 스프링 빈으로 등록
        @Bean
        public DashboardRepository dashboardRepository() {
            return Mockito.mock(DashboardRepository.class);
        }

        // DashboardService는 dashboardRepository 빈을 주입받아 생성
        @Bean
        public DashboardService dashboardService(DashboardRepository dashboardRepository) {
            return new DashboardService(dashboardRepository);
        }
    }

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private DashboardRepository dashboardRepository;

    @Test
    void testFetchDashboardWithCache() {
        // Arrange
        FetchDashboardRequest request = new FetchDashboardRequest();
        List<Dashboard> expectedDashboard = new ArrayList<>();
        when(dashboardRepository.fetchDashboard(request)).thenReturn(expectedDashboard);

        // Act
        List<Dashboard> result1 = dashboardService.fetchDashboardWithCache(request);
        List<Dashboard> result2 = dashboardService.fetchDashboardWithCache(request);

        // Assert: 캐시가 적용되면 동일한 요청에 대해 repository의 fetchDashboard()는 한 번만 호출되어야 합니다.
        assertEquals(expectedDashboard, result1, "First call should return expected Dashboard");
        assertEquals(expectedDashboard, result2, "Second call should return expected Dashboard");
        verify(dashboardRepository, times(1)).fetchDashboard(request);
    }

}
