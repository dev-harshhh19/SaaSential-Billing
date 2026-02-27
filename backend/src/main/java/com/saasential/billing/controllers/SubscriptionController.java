package com.saasential.billing.controllers;

import com.saasential.billing.dto.ApiResponse;
import com.saasential.billing.dto.SubscriptionRequest;
import com.saasential.billing.models.Subscription;
import com.saasential.billing.security.CustomUserDetails;
import com.saasential.billing.services.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

  @Autowired
  private SubscriptionService subscriptionService;

  @PostMapping
  public ResponseEntity<ApiResponse<Subscription>> subscribe(
      @Valid @RequestBody SubscriptionRequest request,
      Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    Subscription subscription = subscriptionService.createSubscription(userDetails.getId(), request);
    return ResponseEntity.ok(ApiResponse.success("Subscribed successfully", subscription));
  }

  @PutMapping("/{id}/upgrade")
  public ResponseEntity<ApiResponse<Subscription>> upgradeDowngrade(
      @PathVariable Long id,
      @Valid @RequestBody SubscriptionRequest request) {
    Subscription subscription = subscriptionService.upgradeDowngradePlan(id, request);
    return ResponseEntity.ok(ApiResponse.success("Plan changed successfully", subscription));
  }

  @PutMapping("/{id}/cancel")
  public ResponseEntity<ApiResponse<Subscription>> cancelSubscription(@PathVariable Long id) {
    Subscription subscription = subscriptionService.cancelSubscription(id);
    return ResponseEntity.ok(ApiResponse.success("Subscription cancelled successfully", subscription));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<ApiResponse<List<Subscription>>> getUserSubscriptions(@PathVariable Long userId) {
    List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(userId);
    return ResponseEntity.ok(ApiResponse.success("Fetched user subscriptions successfully", subscriptions));
  }
}
