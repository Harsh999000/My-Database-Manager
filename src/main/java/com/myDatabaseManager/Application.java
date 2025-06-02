package com.myDatabaseManager;
/*
Package name to help the java complier identify where this file lives when 
imported by other classes. The other classes can import this using this name 
*/

import org.springframework.boot.SpringApplication;
/*
Import Spring Boot class - to bootstrap and start the application.
Maven downloads the dependency as per the pom.xml. As per the springboot dependency
the file downloaded is: spring-boot-<version>.jar
This files is stored in local maven repository and during compilation the IDE adds
these jars to the classpath so that Java can find SpringApplication and other classes 
*/

import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
Import main annotation
@SpringBootApplication is an annotation in the Spring Boot framework. It’s a 
convenience annotation that bundles together three core Spring annotations:
@SpringBootApplication =
  @Configuration - Marks the class as a source of Spring beans (like a config file).
  + @EnableAutoConfiguration - Tells Spring Boot to automatically configure your 
        app based on the dependencies on the classpath. (E.g., if you have Spring 
        Web, it sets up Tomcat and MVC automatically.)
  + @ComponentScan - Automatically scans your package and registers beans 
        (like @Controller, @Service, @Repository) so we don’t have to declare 
        them manually.
*/

/*
This is the main entry point for your Spring Boot application.
It tells Spring Boot to start up and configure everything.
 */
@SpringBootApplication
public class Application {

    /*
     * Standard Java main() method.
     * This is where the JVM starts the app.
     */
    public static void main(String[] args) {
        /*
         * Spring Boot's magic - it starts the embedded web server (Tomcat)
         * and loads all your @Controller, @Service, @Repository classes
         */
        SpringApplication.run(Application.class, args);
        /*
         * When Spring Boot starts, it sets up the embedded server
         * (Tomcat, Jetty, etc.
         * then loads the Spring application context (including controllers). And
         * then registers DispatcherServlet to handle all incoming HTTP requests.
         * So afer this it doesn’t automatically call a controller at startup. It
         * waits for an HTTP request (like from the browser).
         */
    }
}
