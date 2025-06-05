package com.myDatabaseManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuperAdminController {

    @GetMapping("/super_admin/dashboard")
    public String superAdminDashboard() {
        return "super_admin_dashboard"; // Renders super_admin_dashboard.html
    }
}
