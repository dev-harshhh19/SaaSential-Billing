package com.saasential.billing.controllers;

import com.saasential.billing.dto.ApiResponse;
import com.saasential.billing.dto.DashboardMetrics;
import com.saasential.billing.services.AdminAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

  @Autowired
  private AdminAnalyticsService adminAnalyticsService;

  @GetMapping
  public ResponseEntity<ApiResponse<DashboardMetrics>> getDashboardOverview() {
    DashboardMetrics metrics = adminAnalyticsService.getDashboardMetrics();
    return ResponseEntity.ok(ApiResponse.<DashboardMetrics>builder()
        .status(200)
        .message("Fetched analytics successfully")
        .data(metrics)
        .build());
  }
}
