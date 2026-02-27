package com.saasential.billing.services;

import com.saasential.billing.exceptions.ResourceNotFoundException;

import com.saasential.billing.dto.UsageRequest;
import com.saasential.billing.models.Subscription;
import com.saasential.billing.models.UsageRecord;
import com.saasential.billing.repositories.SubscriptionRepository;
import com.saasential.billing.repositories.UsageRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsageService {

  @Autowired
  private UsageRecordRepository usageRecordRepository;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Transactional
  public UsageRecord recordUsage(UsageRequest request) {
    Subscription subscription = subscriptionRepository.findById(request.getSubscriptionId())
        .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

    LocalDate today = LocalDate.now();
    Optional<UsageRecord> existingRecord = usageRecordRepository
        .findBySubscriptionIdAndRecordedDate(subscription.getId(), today);

    UsageRecord record;
    if (existingRecord.isPresent()) {
      record = existingRecord.get();
      record.setApiUsed(record.getApiUsed() + request.getApiCalls());
      record.setStorageUsed(record.getStorageUsed() + request.getStorageBytes());
    } else {
      record = UsageRecord.builder()
          .subscription(subscription)
          .apiUsed(request.getApiCalls())
          .storageUsed(request.getStorageBytes())
          .recordedDate(today)
          .build();
    }

    return usageRecordRepository.save(record);
  }

  public List<UsageRecord> getUsageBySubscription(Long subscriptionId) {
    return usageRecordRepository.findBySubscriptionId(subscriptionId);
  }
}
