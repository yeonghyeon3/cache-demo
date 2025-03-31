package com.example.cachedemo.domain.dashboard.repository;


import com.example.cachedemo.domain.dashboard.model.Dashboard;
import com.example.cachedemo.interfaces.dto.FetchDashboardRequest;

import java.util.List;

public interface DashboardRepository {

    public List<Dashboard> fetchDashboard(FetchDashboardRequest request);

    public void updateDashboard();
}
