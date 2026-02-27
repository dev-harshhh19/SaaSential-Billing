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
    // Anyone can view active plans, could add logic to separate if user is admin vs
    // user
    List<Plan> plans = planService.getActivePlans();
    return ResponseEntity.ok(ApiResponse.<List<Plan>>builder()
        .status(200)
        .message("Fetched plans successfully")
        .data(plans)
        .build());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  public ResponseEntity<ApiResponse<List<Plan>>> getAllPlansAdmin() {
    List<Plan> plans = planService.getAllPlans();
    return ResponseEntity.ok(ApiResponse.<List<Plan>>builder()
        .status(200)
        .message("Fetched all plans successfully")
        .data(plans)
        .build());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ApiResponse<Plan>> createPlan(@Valid @RequestBody PlanRequest planRequest) {
    Plan createdPlan = planService.createPlan(planRequest);
    return ResponseEntity.ok(ApiResponse.<Plan>builder()
        .status(200)
        .message("Plan created successfully")
        .data(createdPlan)
        .build());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Plan>> updatePlan(@PathVariable Long id,
      @Valid @RequestBody PlanRequest planRequest) {
    Plan updatedPlan = planService.updatePlan(id, planRequest);
    return ResponseEntity.ok(ApiResponse.<Plan>builder()
        .status(200)
        .message("Plan updated successfully")
        .data(updatedPlan)
        .build());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable Long id) {
    planService.deletePlan(id);
    return ResponseEntity.ok(ApiResponse.<Void>builder()
        .status(200)
        .message("Plan deleted/deactivated successfully")
        .build());
  }
}
