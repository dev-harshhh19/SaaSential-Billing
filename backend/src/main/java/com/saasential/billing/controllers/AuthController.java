package com.saasential.billing.controllers;

import com.saasential.billing.dto.ApiResponse;
import com.saasential.billing.dto.AuthResponse;
import com.saasential.billing.dto.LoginRequest;
import com.saasential.billing.dto.RegisterRequest;
import com.saasential.billing.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      AuthResponse authResponse = authService.login(loginRequest);
      return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
          .status(200)
          .message("Login successful")
          .data(authResponse)
          .build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.<AuthResponse>builder()
          .status(400)
          .error("Authentication Failed")
          .message(e.getMessage())
          .build());
    }
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
    try {
      AuthResponse authResponse = authService.register(registerRequest);
      return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
          .status(200)
          .message("Registration successful")
          .data(authResponse)
          .build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ApiResponse.<AuthResponse>builder()
          .status(400)
          .error("Registration Failed")
          .message(e.getMessage())
          .build());
    }
  }
}
