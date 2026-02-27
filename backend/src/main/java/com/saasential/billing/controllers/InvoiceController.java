package com.saasential.billing.controllers;

import com.saasential.billing.dto.ApiResponse;
import com.saasential.billing.models.Invoice;
import com.saasential.billing.models.Payment;
import com.saasential.billing.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

  @Autowired
  private InvoiceService invoiceService;

  @GetMapping("/subscription/{subscriptionId}")
  public ResponseEntity<ApiResponse<Page<Invoice>>> getInvoicesBySubscription(
      @PathVariable Long subscriptionId,
      @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
    Page<Invoice> invoices = invoiceService.getSubscriptionInvoices(subscriptionId, pageable);
    return ResponseEntity.ok(ApiResponse.success("Fetched invoices successfully", invoices));
  }

  @GetMapping("/{invoiceId}")
  public ResponseEntity<ApiResponse<Invoice>> getInvoiceById(@PathVariable Long invoiceId) {
    Invoice invoice = invoiceService.getInvoiceById(invoiceId);
    return ResponseEntity.ok(ApiResponse.<Invoice>builder()
        .status(200)
        .message("Fetched invoice successfully")
        .data(invoice)
        .build());
  }

  @PutMapping("/{invoiceId}/pay")
  public ResponseEntity<ApiResponse<Payment>> payInvoice(
      @PathVariable Long invoiceId,
      @RequestParam(defaultValue = "CREDIT_CARD") String paymentMethod) {

    Payment payment = invoiceService.payInvoice(invoiceId, paymentMethod);
    return ResponseEntity.ok(ApiResponse.<Payment>builder()
        .status(200)
        .message("Invoice paid successfully")
        .data(payment)
        .build());
  }
}
