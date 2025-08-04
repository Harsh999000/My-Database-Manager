package com.harsh.my_database_manager.interceptor;

import com.harsh.my_database_manager.service.ApiAuditService;
import com.harsh.my_database_manager.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiLoggingInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApiAuditService apiAuditService;

    private String detectSourceType(String userAgent) {
        if (userAgent == null)
            return "unknown";
        if (userAgent.contains("Postman"))
            return "postman";
        if (userAgent.contains("Mozilla"))
            return "web_ui";
        if (userAgent.contains("curl"))
            return "curl_script";
        if (userAgent.contains("python"))
            return "python_script";
        return "other";
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {

        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String username = jwtUtil.getUsernameFromToken(token);
            String endpoint = request.getRequestURI();
            String method = request.getMethod();
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            String sourceType = detectSourceType(userAgent);
            int statusCode = response.getStatus();

            apiAuditService.logApiCall(
                    username,
                    endpoint,
                    method,
                    ipAddress,
                    userAgent,
                    sourceType,
                    statusCode,
                    username // created_by
            );
        } catch (Exception e) {
            // Fail silently — we don’t want audit logging to break user flow
            e.printStackTrace();
        }
    }
}
