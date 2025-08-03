package com.harsh.my_database_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard/root")
    public String rootDashboard() {
        return "root_dashboard"; // Resolves to dashboard-root.html
    }

    @GetMapping("/dashboard/superadmin")
    public String superAdminDashboard() {
        return "super_admin_dashboard";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard() {
        return "admin_dashboard";
    }

    @GetMapping("/dashboard/executive")
    public String executiveDashboard() {
        return "executive_dashboard";
    }
}
