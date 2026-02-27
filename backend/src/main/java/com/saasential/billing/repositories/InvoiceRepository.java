package com.saasential.billing.repositories;

import com.saasential.billing.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findBySubscriptionId(Long subscriptionId, Pageable pageable);
    List<Invoice> findBySubscriptionId(Long subscriptionId);
    boolean existsByInvoiceNumber(String invoiceNumber);
}
