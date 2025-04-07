package com.example.cachedemo.application;

import com.example.cachedemo.domain.dashboard.model.Dashboard;
import com.example.cachedemo.domain.dashboard.repository.DashboardRepository;
import com.example.cachedemo.interfaces.dto.FetchDashboardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final CacheManager cacheManager;
    @Transactional(readOnly = true)
    public List<Dashboard> fetchDashboardWithoutCache(FetchDashboardRequest request) {

        return dashboardRepository.fetchDashboard(request);
    }

    @Cacheable(value = "dashboardData", key = "#request.toString()")
    @Transactional(readOnly = true)
    public List<Dashboard> fetchDashboardWithCache(FetchDashboardRequest request) {

        return dashboardRepository.fetchDashboard(request);
    }

    @Transactional
    public void updateDashboardWithoutCacheEvict() {
        dashboardRepository.updateDashboard();
    }

    @Transactional
    @CacheEvict(value = "dashboardData", allEntries = true)
    public void updateDashboardWithCacheEvict() {
        dashboardRepository.updateDashboard();
    }

    @Transactional
    public void updateDashboardWithCacheUpdate() {
        // 내부 데이터 변경
        dashboardRepository.updateDashboard();

        FetchDashboardRequest request = new FetchDashboardRequest();

        Cache cache = cacheManager.getCache("dashboardData");
        if (cache != null) {
            cache.evict(request.toString());
        }

        // 최신 데이터 조회 후 캐시에 직접 저장
        List<Dashboard> newData = dashboardRepository.fetchDashboard(request);
        if (cache != null) {
            cache.put(request.toString(), newData);
        }
    }
}
