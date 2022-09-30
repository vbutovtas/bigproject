package com.project.integration.web.controller;

import com.project.integration.serv.services.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ModelController {
  private final RoleService roleService;
  
  public ModelController(RoleService roleService){
    this.roleService=roleService;
  }
  @GetMapping("/")
  public String landingHome(Model model) {
    return "landing_home";
  }

  @GetMapping("/check")
  @ResponseBody
  public boolean check(){
    return roleService.findByName("q");
  }
}
