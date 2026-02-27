package com.saasential.billing.services;

import com.saasential.billing.exceptions.ResourceNotFoundException;

import com.saasential.billing.dto.PlanRequest;
import com.saasential.billing.models.Plan;
import com.saasential.billing.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

  @Autowired
  private PlanRepository planRepository;

  public Plan createPlan(PlanRequest planRequest) {
    Plan plan = Plan.builder()
        .name(planRequest.getName())
        .price(planRequest.getPrice())
        .billingCycle(planRequest.getBillingCycle())
        .apiLimit(planRequest.getApiLimit())
        .storageLimit(planRequest.getStorageLimit())
        .extraApiCost(planRequest.getExtraApiCost())
        .extraStorageCost(planRequest.getExtraStorageCost())
        .active(planRequest.isActive())
        .trialDays(planRequest.getTrialDays())
        .build();
    return planRepository.save(plan);
  }

  public List<Plan> getAllPlans() {
    return planRepository.findAll();
  }

  public List<Plan> getActivePlans() {
    return planRepository.findByActiveTrue();
  }

  public Plan getPlanById(Long id) {
    return planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
  }

  public Plan updatePlan(Long id, PlanRequest planRequest) {
    Plan plan = getPlanById(id);
    plan.setName(planRequest.getName());
    plan.setPrice(planRequest.getPrice());
    plan.setBillingCycle(planRequest.getBillingCycle());
    plan.setApiLimit(planRequest.getApiLimit());
    plan.setStorageLimit(planRequest.getStorageLimit());
    plan.setExtraApiCost(planRequest.getExtraApiCost());
    plan.setExtraStorageCost(planRequest.getExtraStorageCost());
    plan.setActive(planRequest.isActive());
    plan.setTrialDays(planRequest.getTrialDays());
    return planRepository.save(plan);
  }

  public void deletePlan(Long id) {
    Plan plan = getPlanById(id);
    plan.setActive(false);
    planRepository.save(plan); // Soft delete
  }
}
