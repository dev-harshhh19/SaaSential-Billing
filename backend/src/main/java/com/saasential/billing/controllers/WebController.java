package com.saasential.billing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  @GetMapping("/")
  public String index() {
    return "index"; // landing page & login
  }

  @GetMapping("/dashboard")
  public String dashboard() {
    return "dashboard"; // user dashboard
  }

  @GetMapping("/admin-dashboard")
  public String adminDashboard() {
    return "admin"; // admin dashboard
  }
}
