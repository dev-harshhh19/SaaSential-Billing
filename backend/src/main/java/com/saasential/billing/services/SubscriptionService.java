package com.saasential.billing.services;

import com.saasential.billing.exceptions.ResourceNotFoundException;

import com.saasential.billing.dto.SubscriptionRequest;
import com.saasential.billing.enums.BillingCycle;
import com.saasential.billing.enums.SubscriptionStatus;
import com.saasential.billing.models.Plan;
import com.saasential.billing.models.Subscription;
import com.saasential.billing.models.User;
import com.saasential.billing.repositories.PlanRepository;
import com.saasential.billing.repositories.SubscriptionRepository;
import com.saasential.billing.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionService {

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlanRepository planRepository;

  @Transactional
  public Subscription createSubscription(Long userId, SubscriptionRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Plan plan = planRepository.findById(request.getPlanId())
        .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

    // Check active subscription
    var activeSub = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
    if (activeSub.isPresent()) {
      throw new IllegalArgumentException("User already has an active subscription");
    }

    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime endDate = plan.getBillingCycle() == BillingCycle.MONTHLY
        ? startDate.plusMonths(1)
        : startDate.plusYears(1);

    Subscription subscription = Subscription.builder()
        .user(user)
        .plan(plan)
        .startDate(startDate)
        .endDate(endDate)
        .autoRenew(true)
        .status(plan.getTrialDays() > 0 ? SubscriptionStatus.TRIAL : SubscriptionStatus.ACTIVE)
        .trialEndDate(plan.getTrialDays() > 0 ? startDate.plusDays(plan.getTrialDays()) : null)
        .build();

    return subscriptionRepository.save(subscription);
  }

  @Transactional
  public Subscription upgradeDowngradePlan(Long subscriptionId, SubscriptionRequest request) {
    Subscription subscription = subscriptionRepository.findById(subscriptionId)
        .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

    Plan newPlan = planRepository.findById(request.getPlanId())
        .orElseThrow(() -> new ResourceNotFoundException("New Plan not found"));

    subscription.setPlan(newPlan);

    // Handling proration in a real app would be complex. For this MVP we just reset
    // dates or apply immediately
    subscription.setStartDate(LocalDateTime.now());
    subscription.setEndDate(newPlan.getBillingCycle() == BillingCycle.MONTHLY ? LocalDateTime.now().plusMonths(1)
        : LocalDateTime.now().plusYears(1));

    return subscriptionRepository.save(subscription);
  }

  @Transactional
  public Subscription cancelSubscription(Long subscriptionId) {
    Subscription subscription = subscriptionRepository.findById(subscriptionId)
        .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

    subscription.setAutoRenew(false);
    subscription.setStatus(SubscriptionStatus.CANCELLED);
    return subscriptionRepository.save(subscription);
  }

  public List<Subscription> getUserSubscriptions(Long userId) {
    return subscriptionRepository.findByUserId(userId);
  }
}
