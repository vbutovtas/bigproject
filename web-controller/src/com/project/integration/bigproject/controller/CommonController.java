package com.project.integration.bigproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CommonController {
    @RequestMapping("/")
    public String landingHome(Model model) {
        return "landing_home";
    }
}
