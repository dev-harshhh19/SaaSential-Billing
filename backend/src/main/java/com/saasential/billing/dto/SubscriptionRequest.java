package com.saasential.billing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {
  @NotNull(message = "Plan ID is required")
  private Long planId;
}
