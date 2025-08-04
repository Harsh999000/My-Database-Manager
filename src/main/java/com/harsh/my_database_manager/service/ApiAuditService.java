package com.harsh.my_database_manager.service;

public interface ApiAuditService {

    /**
     * Logs a successful API call to the audit table.
     *
     * @param username   Authenticated username (from JWT)
     * @param endpoint   API path hit (e.g., /api/create-user)
     * @param method     HTTP method (GET, POST, etc.)
     * @param ip         Client IP address
     * @param userAgent  Client's User-Agent string
     * @param sourceType Inferred origin (web_ui, postman, etc.)
     * @param statusCode HTTP response code (e.g., 200, 401)
     * @param createdBy  Who initiated the log (usually same as username)
     */
    void logApiCall(String username,
            String endpoint,
            String method,
            String ip,
            String userAgent,
            String sourceType,
            int statusCode,
            String createdBy);
}
