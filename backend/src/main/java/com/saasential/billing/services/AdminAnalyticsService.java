package com.saasential.billing.services;

import com.saasential.billing.dto.DashboardMetrics;
import com.saasential.billing.enums.InvoiceStatus;
import com.saasential.billing.enums.PaymentStatus;
import com.saasential.billing.enums.SubscriptionStatus;
import com.saasential.billing.models.Invoice;
import com.saasential.billing.models.Payment;
import com.saasential.billing.models.Subscription;
import com.saasential.billing.repositories.InvoiceRepository;
import com.saasential.billing.repositories.PaymentRepository;
import com.saasential.billing.repositories.SubscriptionRepository;
import com.saasential.billing.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminAnalyticsService {

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private InvoiceRepository invoiceRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  public DashboardMetrics getDashboardMetrics() {
    List<Subscription> allSubscriptions = subscriptionRepository.findAll();

    long activeSubscriptionsCount = allSubscriptions.stream()
        .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE)
        .count();

    // Basic MRR Calculation: Sum of prices of active plans. Actual implementation
    // may do monthly proration mapping.
    BigDecimal mrr = allSubscriptions.stream()
        .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE)
        .map(sub -> sub.getPlan().getPrice())
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    long totalUsers = userRepository.count();

    List<Payment> payments = paymentRepository.findAll();
    long failedPayments = payments.stream()
        .filter(p -> p.getPaymentStatus() == PaymentStatus.FAILED)
        .count();

    return DashboardMetrics.builder()
        .monthlyRecurringRevenue(mrr)
        .activeSubscriptions(activeSubscriptionsCount)
        .totalUsers(totalUsers)
        .failedPayments(failedPayments)
        .build();
  }
}
