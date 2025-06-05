package com.myDatabaseManager.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        // Get the user’s role
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // Redirect based on the role
        if (role.equals("ROLE_ROOT")) {
            response.sendRedirect("/root/dashboard");
        } else if (role.equals("ROLE_SUPER_ADMIN")) {
            response.sendRedirect("/super_admin/dashboard");
        } else if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (role.equals("ROLE_EXECUTIVE")) {
            response.sendRedirect("/executive/dashboard");
        } else {
            // Fallback: redirect to home or error page
            response.sendRedirect("/");
        }
    }
}
