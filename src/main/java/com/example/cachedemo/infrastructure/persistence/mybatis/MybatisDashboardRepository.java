package com.example.cachedemo.infrastructure.persistence.mybatis;


import com.example.cachedemo.domain.dashboard.model.Dashboard;
import com.example.cachedemo.domain.dashboard.repository.DashboardRepository;
import com.example.cachedemo.interfaces.dto.FetchDashboardRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface MybatisDashboardRepository extends DashboardRepository {

    public List<Dashboard> fetchDashboard(FetchDashboardRequest request);

    public void updateDashboard();

}
