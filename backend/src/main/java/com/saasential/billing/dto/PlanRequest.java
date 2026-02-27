package com.saasential.billing.dto;

import com.saasential.billing.enums.BillingCycle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlanRequest {
  @NotBlank(message = "Plan name is required")
  private String name;

  @NotNull(message = "Price is required")
  private BigDecimal price;

  @NotNull(message = "Billing cycle is required")
  private BillingCycle billingCycle;

  @NotNull(message = "API limit is required")
  private Integer apiLimit;

  @NotNull(message = "Storage limit is required")
  private Integer storageLimit;

  @NotNull(message = "Extra API cost is required")
  private BigDecimal extraApiCost;

  @NotNull(message = "Extra storage cost is required")
  private BigDecimal extraStorageCost;

  private boolean active = true;

  private int trialDays = 0;
}
