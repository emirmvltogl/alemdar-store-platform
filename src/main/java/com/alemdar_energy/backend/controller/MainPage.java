package com.alemdar_energy.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainPage {
  
  @GetMapping
  public String getPage(Model model){
    return "products/main";
  }
}
