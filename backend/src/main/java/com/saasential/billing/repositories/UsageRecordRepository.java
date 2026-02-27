package com.saasential.billing.repositories;

import com.saasential.billing.models.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {
    List<UsageRecord> findBySubscriptionId(Long subscriptionId);
    
    Optional<UsageRecord> findBySubscriptionIdAndRecordedDate(Long subscriptionId, LocalDate date);
}
