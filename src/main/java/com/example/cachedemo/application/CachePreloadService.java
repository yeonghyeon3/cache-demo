package com.example.cachedemo.application;

import com.example.cachedemo.domain.dashboard.model.Dimensions;
import com.example.cachedemo.domain.mysqlcache.MysqlCacheManager;
import com.example.cachedemo.interfaces.dto.FetchDashboardRequest;
import org.springframework.stereotype.Service;

@Service
public class CachePreloadService {
    private final MysqlCacheManager mysqlCacheManager;

    private final DashboardService dashboardService;

    public CachePreloadService(MysqlCacheManager mysqlCacheManager, DashboardService dashboardService) {
        this.mysqlCacheManager = mysqlCacheManager;
        this.dashboardService = dashboardService;
    }

    public void init() {

        mysqlCacheManager.clearAll();
        dashboardService.fetchDashboardWithCache(new FetchDashboardRequest());
        FetchDashboardRequest fetchDashboardRequest = new FetchDashboardRequest();
        fetchDashboardRequest.setDimension1(Dimensions.CharlieDimension.name());
        dashboardService.fetchDashboardWithCache(fetchDashboardRequest);
    }

}
