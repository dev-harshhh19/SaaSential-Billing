package com.saasential.billing.controllers;

import com.saasential.billing.dto.ApiResponse;
import com.saasential.billing.dto.PlanRequest;
import com.saasential.billing.models.Plan;
import com.saasential.billing.services.PlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

  @Autowired
  private PlanService planService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<Plan>>> getAllPlans() {
    List<Plan> plans = planService.getActivePlans();
    return ResponseEntity.ok(ApiResponse.success("Fetched plans successfully", plans));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  public ResponseEntity<ApiResponse<List<Plan>>> getAllPlansAdmin() {
    List<Plan> plans = planService.getAllPlans();
    return ResponseEntity.ok(ApiResponse.success("Fetched all plans successfully", plans));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ApiResponse<Plan>> createPlan(@Valid @RequestBody PlanRequest planRequest) {
    Plan createdPlan = planService.createPlan(planRequest);
    return ResponseEntity.ok(ApiResponse.success("Plan created successfully", createdPlan));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Plan>> updatePlan(@PathVariable Long id,
      @Valid @RequestBody PlanRequest planRequest) {
    Plan updatedPlan = planService.updatePlan(id, planRequest);
    return ResponseEntity.ok(ApiResponse.success("Plan updated successfully", updatedPlan));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable Long id) {
    planService.deletePlan(id);
    return ResponseEntity.ok(ApiResponse.success("Plan deleted/deactivated successfully"));
  }
}
