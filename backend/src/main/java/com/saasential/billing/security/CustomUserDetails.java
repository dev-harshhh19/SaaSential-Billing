package com.saasential.billing.security;

import com.saasential.billing.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

  private final Long id;
  private final String name;
  private final String email;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;

  public CustomUserDetails(Long id, String name, String email, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public static CustomUserDetails create(User user) {
    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

    return new CustomUserDetails(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getPassword(),
        Collections.singletonList(authority));
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
