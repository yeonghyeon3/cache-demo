package com.example.cachedemo.infrastructure.persistence.mybatis;

import com.example.cachedemo.domain.mysqlcache.repository.MysqlCacheRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MybatisMysqlCacheRepository extends MysqlCacheRepository {}
