package com.harsh.my_database_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToHtmlIndex() {
        return "forward:/html/index.html";
    }
}
