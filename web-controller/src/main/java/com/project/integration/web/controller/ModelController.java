package com.project.integration.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModelController {
  @GetMapping("/")
  public String landingHome(Model model) {
    return "landing_home";
  }
}
