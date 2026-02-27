package com.saasential.billing.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardMetrics {
  private BigDecimal monthlyRecurringRevenue; // MRR
  private long activeSubscriptions;
  private long totalUsers;
  private long failedPayments;
}
