package com.saasential.billing.scheduler;

import com.saasential.billing.enums.BillingCycle;
import com.saasential.billing.enums.SubscriptionStatus;
import com.saasential.billing.models.Invoice;
import com.saasential.billing.models.Subscription;
import com.saasential.billing.repositories.SubscriptionRepository;
import com.saasential.billing.services.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SubscriptionRenewalScheduler {

  private static final Logger logger = LoggerFactory.getLogger(SubscriptionRenewalScheduler.class);

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private InvoiceService invoiceService;

  // Runs every day at midnight
  @Scheduled(cron = "0 0 0 * * ?")
  public void processAutoRenewalsAndInvoices() {
    logger.info("Executing auto-renewal and invoice generation scheduler...");

    LocalDateTime today = LocalDateTime.now();

    // 1. Convert expired Trial bounds into Active if AutoRenew is ON, etc.
    // For simplicity, find active subscriptions ending before or exactly at today
    List<Subscription> expiringSubscriptions = subscriptionRepository.findByStatusAndEndDateBefore(
        SubscriptionStatus.ACTIVE, today.plusDays(1));

    for (Subscription sub : expiringSubscriptions) {
      if (sub.isAutoRenew()) {
        logger.info("Renewing subscription ID: " + sub.getId());

        // Generate invoice for concluding period
        invoiceService.generateInvoice(sub, sub.getStartDate(), sub.getEndDate());

        // Update dates for next cycle
        sub.setStartDate(sub.getEndDate());
        sub.setEndDate(sub.getPlan().getBillingCycle() == BillingCycle.MONTHLY
            ? sub.getEndDate().plusMonths(1)
            : sub.getEndDate().plusYears(1));

        subscriptionRepository.save(sub);
      } else {
        logger.info("Expiring subscription ID (no auto-renew): " + sub.getId());
        // Generate final invoice and mark expired
        invoiceService.generateInvoice(sub, sub.getStartDate(), sub.getEndDate());
        sub.setStatus(SubscriptionStatus.EXPIRED);
        subscriptionRepository.save(sub);
      }
    }

    logger.info("Scheduler execution completed.");
  }
}
