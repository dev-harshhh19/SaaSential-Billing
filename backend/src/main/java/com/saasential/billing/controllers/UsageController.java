package com.saasential.billing.controllers;

import com.saasential.billing.dto.ApiResponse;
import com.saasential.billing.dto.UsageRequest;
import com.saasential.billing.models.UsageRecord;
import com.saasential.billing.services.UsageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usage")
public class UsageController {

  @Autowired
  private UsageService usageService;

  @PostMapping("/record")
  public ResponseEntity<ApiResponse<UsageRecord>> recordUsage(@Valid @RequestBody UsageRequest request) {
    UsageRecord record = usageService.recordUsage(request);
    return ResponseEntity.ok(ApiResponse.<UsageRecord>builder()
        .status(200)
        .message("Usage recorded successfully")
        .data(record)
        .build());
  }

  @GetMapping("/{subscriptionId}")
  public ResponseEntity<ApiResponse<List<UsageRecord>>> getUsage(@PathVariable Long subscriptionId) {
    List<UsageRecord> records = usageService.getUsageBySubscription(subscriptionId);
    return ResponseEntity.ok(ApiResponse.<List<UsageRecord>>builder()
        .status(200)
        .message("Fetched usage successfully")
        .data(records)
        .build());
  }
}
