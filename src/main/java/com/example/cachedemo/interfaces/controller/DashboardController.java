package com.example.cachedemo.interfaces.controller;

import com.example.cachedemo.application.DashboardService;
import com.example.cachedemo.domain.dashboard.model.Dashboard;
import com.example.cachedemo.interfaces.dto.FetchDashboardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/fetch-without-cache")
    public ResponseEntity<List<Dashboard>> fetchDashboardWithoutCache(@ModelAttribute FetchDashboardRequest request) {

        List<Dashboard> response = dashboardService.fetchDashboardWithoutCache(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetch-with-cache")
    public ResponseEntity<List<Dashboard>> fetchDashboardWithCache(@ModelAttribute FetchDashboardRequest request) {

        List<Dashboard> response = dashboardService.fetchDashboardWithCache(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-without-cacheevict")
    public ResponseEntity<String> updateDashboardWithoutCacheEvict() {

        dashboardService.updateDashboardWithoutCacheEvict();

        return ResponseEntity.ok("update success!!");
    }

    @PutMapping("/update-with-cacheevict")
    public ResponseEntity<String> updateDashboardWithCacheEvict() {

        dashboardService.updateDashboardWithCacheEvict();

        return ResponseEntity.ok("update success!!");
    }

    @PutMapping("/update-with-cacheupdate")
    public ResponseEntity<String> updateDashboardWithCacheUpdate() {
        dashboardService.updateDashboardWithCacheUpdate();
        return ResponseEntity.ok("update success!!");
    }
}

