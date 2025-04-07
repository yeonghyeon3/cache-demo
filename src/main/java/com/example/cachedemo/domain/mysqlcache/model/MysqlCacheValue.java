package com.example.cachedemo.domain.mysqlcache.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MysqlCacheValue {
    private final byte[] data;

    public boolean isEmpty() {
        return data == null || data.length == 0;
    }
}
