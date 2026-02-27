package com.saasential.billing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsageRequest {
  @NotNull(message = "Subscription ID is required")
  private Long subscriptionId;

  private int apiCalls = 0;

  private int storageBytes = 0;
}
