package com.myDatabaseManager.controller;
/*
 * This specifies the package name. 
 * This file is in the controller subpackage of com.myDatabaseManager.
 * It helps to organizes classes related to the controller layer of the 
 * application, following the MVC (Model-View-Controller) pattern.
 */

import org.springframework.stereotype.Controller;
/*
 * org.springframework - This is the base package of the Spring Framework.
 * org.springframework.stereotype is part of Spring Framework’s core 
 * annotations package. Controller here in this is a Java annotation 
 * (also called a marker or metadata annotation).
 * This is used to indicate the purpose of the class as a controller
 * This basically says hey this class ia a controller in MVC pattern
 */

import org.springframework.web.bind.annotation.GetMapping;
/*
 * org.springframework - This is the base package of the Spring Framework.
 * web - Subpackage containing classes related to web (HTTP) interactions in Spring.
 * bind.annotation - Subpackage containing annotations used to map HTTP requests 
 * to Java methods.
 * GetMapping - The specific annotation that handles HTTP GET requests.
 */

@Controller
/*
 * '@Controller' tells spring that this class handles web requests.
 * Spring automatically scans for @Controller classes and uses them to map
 * incoming requests.
 */

public class IndexController {
    /*
     * The name IndexController is basic naming convention for handling index
     * or home-related queries
     */

    @GetMapping({ "/", "/login" })
    /*
     * This method will handle get requests for '/' which is root url and '/login'
     */

    public String index() {
        /*
         * String return type tells Spring which view (HTML Template) to render
         * This is searched in resources/template folder
         */
        return "index";
        // Tells Spring Boot to render index.html from templates
    }
}
