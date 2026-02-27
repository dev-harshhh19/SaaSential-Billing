package com.saasential.billing.services;

import com.saasential.billing.exceptions.ResourceNotFoundException;

import com.saasential.billing.dto.AuthResponse;
import com.saasential.billing.dto.LoginRequest;
import com.saasential.billing.dto.RegisterRequest;
import com.saasential.billing.enums.Role;
import com.saasential.billing.models.User;
import com.saasential.billing.repositories.UserRepository;
import com.saasential.billing.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider tokenProvider;

  public AuthResponse login(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);

    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    return AuthResponse.builder()
        .token(jwt)
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .role(user.getRole().name())
        .type("Bearer")
        .build();
  }

  public AuthResponse register(RegisterRequest registerRequest) {
    if (userRepository.existsByEmail(registerRequest.getEmail())) {
      throw new IllegalArgumentException("Email is already taken!");
    }

    User user = User.builder()
        .name(registerRequest.getName())
        .email(registerRequest.getEmail())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .role(registerRequest.getRole() != null ? registerRequest.getRole() : Role.ROLE_USER)
        .enabled(true)
        .build();

    User savedUser = userRepository.save(user);

    // Auto login after register
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            registerRequest.getEmail(),
            registerRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);

    return AuthResponse.builder()
        .token(jwt)
        .id(savedUser.getId())
        .email(savedUser.getEmail())
        .name(savedUser.getName())
        .role(savedUser.getRole().name())
        .type("Bearer")
        .build();
  }
}
