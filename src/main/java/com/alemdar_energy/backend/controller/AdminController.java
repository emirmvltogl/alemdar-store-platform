package com.alemdar_energy.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {


  @GetMapping("/showPanel")
  public String showAdminPanel(Model model){
    return "admin/admin-panel";
  }
}
