package com.example.cachedemo.domain.dashboard.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Dashboard implements Serializable {  // Redis Cache 직렬화
    private static final long serialVersionUID = 1L;

    private String dimension1;
    private String dimension2;
    private String dimension3;
    private String dimension4;
    private String dimension5;
    private String dimension6;
    private String dimension7;
    private String dimension8;
    private String dimension9;
    private String dimension10;

    private Double measure1;
    private Double measure2;
    private Double measure3;
    private Double measure4;
    private Double measure5;
    private Double measure6;
    private Double measure7;
}
