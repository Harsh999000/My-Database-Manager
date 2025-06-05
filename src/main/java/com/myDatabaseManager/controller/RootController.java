package com.myDatabaseManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/root/dashboard")
    public String rootDashboard() {
        return "root_dashboard"; // Renders root_dashboard.html
    }
}
