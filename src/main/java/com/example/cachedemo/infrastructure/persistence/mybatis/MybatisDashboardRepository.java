package com.example.cachedemo.infrastructure.persistence.mybatis;


import com.example.cachedemo.domain.dashboard.repository.DashboardRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface MybatisDashboardRepository extends DashboardRepository {}
