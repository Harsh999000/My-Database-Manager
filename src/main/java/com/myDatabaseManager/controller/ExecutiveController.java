package com.myDatabaseManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExecutiveController {

    @GetMapping("/executive/dashboard")
    public String executiveDashboard() {
        return "executive_dashboard"; // Renders executive_dashboard.html
    }
}
