package com.myDatabaseManager.config;
// This is to declare that this file os part of the config package

import com.myDatabaseManager.service.CustomUserDetailsService;
import com.myDatabaseManager.handler.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Explanation of all the imports
 * com.myDatabaseManager: This is the base package name of this Spring Boot application.
 *  - service: It’s a subpackage of your base package com.myDatabaseManager. In MVC for a three their architecture it interact with your data access layer (like repositories) to perform tasks.
 *      - CustomUserDetailsService: Its a java class in the service package. Its a user-specific data to Spring Security during the authentication process.
 *  - handler: Its a subpackage of com.myDatabaseManager. It typically contains custom event handlers for Spring Security (or other frameworks). Example: CustomAuthenticationSuccessHandler
 *      - CustomAuthenticationSuccessHandler: Its a java class in the handler package. It defines the logic to run after successful login
 * org.springframework: It’s the top-level package of the entire Spring Framework. It contains all the core features of Spring, including:
 *                      - Dependency injection (DI) and bean management
 *                      - Web MVC framework.
 *                      - Security, data access, etc.
 *  - context: Its a subpackage of org.springframework. It contains classes related to the ApplicationContext in Spring, which is the central container for managing beans and dependencies.
 *      - annotation: Its a subpackage which comes under context. It contains annotations for configuring Spring’s ApplicationContext using Java code (instead of XML). Here we declare configuration, beans, and dependency injection directly in your Java classes. Annotations are stored in the bytecode of your program. This is to give extra information to compiler like @Override
 *                    @Deprecated or runtime environment framework like @Controller or @Bean. This helps avoid repetative configuration in XML config.
 *          - Bean: It’s a Java annotation in the org.springframework.context.annotation package. It’s used to tell Spring that the method below creates and returns a Spring-managed bean.” When Spring starts up, it scans for @Configuration classes. If it finds a method annotated with 
 *                  @Bean inside those classes, it calls that method. It takes the returned object and registers it as a bean in the Spring ApplicationContext. In the Spring Framework, a bean is simply an object that is 
 *                  managed by the Spring ApplicationContext (the central container) “managed by Spring” this basically means when an object is a bean, then Spring:
 *                  - Creates the object (instantiation)
 *                  - Configures it (sets dependencies / injects other beans it needs)
 *                  - Manages its lifecycle (like starting it up, shutting it down properly)
 *                  And since spring injects beans where they’re needed, so you don’t have to manually create objects. Also, pring takes care of creating beans when the app starts and cleaning them up when it shuts down.
 *          - Configuration: It is also a java annotation and marks a class as configuration class in sprnig application. You use this to tell spring that this class contains bean defination and configuration logic for application. So spring will scan this class during startup, then find the methods annotated with beans and call those methods and register their values as beans in the spring application context
 *  - security: This is a core package in the Spring Security module. It contains everything related to authentication, authorization, and securing your Spring application.
 *      - authentication: Its a subpackage that contains core classes and interfaces related to authenticating users. It provides the local componenets that verify the username/passwords, user status (enabnled, locked, expired), authentication tokens like JWT
 *          - AuthenticationManager: Its a core interface in Spring Security responsible for authenticating user credentials. You give it an Authentication object (like a username + password). It tries to authenticate the credentials:
 *                                   - If successful, returns an authenticated Authentication object.
 *                                   - If failed, throws an AuthenticationException.
 *          - dao: Its a subpackage in Spring Security. It’s focused on DAO-based authentication. DAO stands for Data Access Object — a design pattern for retrieving and storing data (like users from a database).
 *                 A DAO based authentication fetches user data information from a database instead of storing it in memory. Spring Security uses a UserDetailsService to load user data (username, password, roles) from your database.
 *              - DaoAuthenticationProvider: Its a built-in class in Spring Security. It’s an AuthenticationProvider implementation that:
 *                                           - Uses a UserDetailsService to load user data from the database.
 *                                           - Uses a password encoder (like BCryptPasswordEncoder) to validate passwords.
 *                                           We use this as we do not have to write the login logic ourselves we just provide a UserDetailsService and spring security does the rest.
 *                                           It also automatically uses the PasswordEncoder you configured (like bcrypt) and compares hashed passwords, not plaintext. Hence a better security.
 *      - config: Its a package for Spring Security’s configuration APIs. It contains classes and interfaces that let you configure security settings (like access rules, login, password encoders) in your Spring application.
 *          - annotation: Its a subpackage for annotation-based configuration in Spring Security. I lets you customize security using Java annotations instead of XML.
 *              - authentication: Its a subpackage focused on configuring authentication. I holds classes and interfaces here help you tell Spring Security how to authenticate users (e.g., using databases, LDAP, etc.).
 *                  - configuration: Its a subpackage that helps fine tune the authentication configuration it contains classes and builders that help:
 *                                   - Register authentication providers (like DaoAuthenticationProvider for DB logins).
 *                                   - Connect to your user data (UserDetailsService).
 *                                   - Apply security rules (like password encoders, login URLs).
 *                      - AuthenticationConfiguration: Its a Spring-provided configuration class for authentication. It exposes the AuthenticationManager as a bean that you can inject into other parts of your app. During startup, Spring Security builds an AuthenticationManager based on your UserDetailsService and password encoders. AuthenticationConfiguration provides access to that manager so you can manually authenticate users (e.g., custom login forms).
 *              - web: It is a subpackage for configuring security for web applications. It deals with HTTP-based security (like login pages, access control to URLs, CSRF, CORS, etc.).
 *                  - builders: It is a subpackage that lets you congifure using a chainable style, it contains builder classes like HttpSecurity which helps to configure HTTP security like which URL require login, which role can access what,how to handle login and logout etc.
 *                      - HttpSecurity: It a class which provides API to configure how HTTP security works in the app.
 *                  - configuration: It contains annotations that enable webs security, like @EnableWebSecurity 
 *                      - EnableWebSecurity: Inables Spring Security’s Web Security features for your application. When you add @EnableWebSecurity to a @Configuration class, it tells Spring Boot that this class will provide security configuration (like authentication, authorization, CSRF, etc.) for web requests.
 *      - crypto: It a subpackage containning cryptographic tools and classes. This helps to secure password by hashing passwords (like bcyrpt) and encoding and decoding secure tokens
 *          - bcrypt: It is a subpackage for bcrypt hashing algorithm.
 *              - BCryptPasswordEncoder: It is a class that implements the PasswordEncoder interface. It uses bcrypt to hash and check passwords.
 *          - password: this is a subpackage containing classes for password encoding and verification.
 *              - PasswordEncoder: PasswordEncoder is an interface in Spring Security, designed to provide a standard way to hash (encode) and verify (match) passwords.
 *      - web: This contains all the web-specific security classes. It handles security features for HTTP requests and responses.
 *          - SecurityFilterChain: is a key Spring Security interface that defines how incoming HTTP requests are processed through a chain of security filters.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService,
            CustomAuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Dashboard access restrictions
                        .requestMatchers("/root/**").hasRole("ROOT")
                        .requestMatchers("/super_admin/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/executive/**").hasRole("EXECUTIVE")
                        // Allow everyone to see the login page
                        .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .permitAll())
                .logout(logout -> logout.permitAll())
                .csrf(csrf -> csrf.disable()); // Disable CSRF for testing (not recommended for prod)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
