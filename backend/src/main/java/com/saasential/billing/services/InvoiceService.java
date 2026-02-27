package com.saasential.billing.services;

import com.saasential.billing.exceptions.ResourceNotFoundException;

import com.saasential.billing.enums.InvoiceStatus;
import com.saasential.billing.enums.PaymentStatus;
import com.saasential.billing.models.*;
import com.saasential.billing.repositories.InvoiceRepository;
import com.saasential.billing.repositories.PaymentRepository;
import com.saasential.billing.repositories.SubscriptionRepository;
import com.saasential.billing.repositories.UsageRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

  @Autowired
  private InvoiceRepository invoiceRepository;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private UsageRecordRepository usageRecordRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Transactional
  public Invoice generateInvoice(Subscription subscription, LocalDateTime start, LocalDateTime end) {
    Plan plan = subscription.getPlan();

    // Calculate usage overages
    List<UsageRecord> usages = usageRecordRepository.findBySubscriptionId(subscription.getId());
    int totalApiUsed = usages.stream().mapToInt(UsageRecord::getApiUsed).sum();
    int totalStorageUsed = usages.stream().mapToInt(UsageRecord::getStorageUsed).sum();

    BigDecimal overageAmount = BigDecimal.ZERO;

    if (totalApiUsed > plan.getApiLimit()) {
      int extraApi = totalApiUsed - plan.getApiLimit();
      overageAmount = overageAmount.add(plan.getExtraApiCost().multiply(BigDecimal.valueOf(extraApi)));
    }

    if (totalStorageUsed > plan.getStorageLimit()) {
      int extraStorage = totalStorageUsed - plan.getStorageLimit();
      overageAmount = overageAmount.add(plan.getExtraStorageCost().multiply(BigDecimal.valueOf(extraStorage)));
    }

    BigDecimal baseAmount = plan.getPrice();
    BigDecimal taxAmount = baseAmount.add(overageAmount).multiply(BigDecimal.valueOf(0.18)); // Example 18% tax
    BigDecimal totalAmount = baseAmount.add(overageAmount).add(taxAmount);

    Invoice invoice = Invoice.builder()
        .invoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
        .subscription(subscription)
        .baseAmount(baseAmount)
        .extraCharges(overageAmount)
        .taxAmount(taxAmount)
        .totalAmount(totalAmount)
        .status(InvoiceStatus.PENDING)
        .billingPeriodStart(start)
        .billingPeriodEnd(end)
        .generatedDate(LocalDateTime.now())
        .dueDate(LocalDateTime.now().plusDays(7))
        .build();

    return invoiceRepository.save(invoice);
  }

  @Transactional
  public Payment payInvoice(Long invoiceId, String paymentMethod) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

    if (invoice.getStatus() == InvoiceStatus.PAID) {
      throw new IllegalArgumentException("Invoice is already paid");
    }

    // Mock payment processing success
    Payment payment = Payment.builder()
        .invoice(invoice)
        .paymentMethod(paymentMethod)
        .paymentStatus(PaymentStatus.SUCCESS)
        .transactionReference("TXN-" + UUID.randomUUID().toString().toUpperCase())
        .amountPaid(invoice.getTotalAmount())
        .paymentDate(LocalDateTime.now())
        .build();

    paymentRepository.save(payment);

    invoice.setStatus(InvoiceStatus.PAID);
    invoiceRepository.save(invoice);

    return payment;
  }

  public Page<Invoice> getSubscriptionInvoices(Long subscriptionId, Pageable pageable) {
    return invoiceRepository.findBySubscriptionId(subscriptionId, pageable);
  }

  public List<Invoice> getSubscriptionInvoicesList(Long subscriptionId) {
    return invoiceRepository.findBySubscriptionId(subscriptionId);
  }

  public Invoice getInvoiceById(Long invoiceId) {
    return invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
  }
}
