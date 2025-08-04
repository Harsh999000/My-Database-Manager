package com.harsh.my_database_manager.service.impl;

import com.harsh.my_database_manager.service.ApiAuditService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class ApiAuditServiceImpl implements ApiAuditService {

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${DB_HOST}")
    private String dbHost;

    @Value("${DB_PORT}")
    private String dbPort;

    private final String AUDIT_DB = "dbmanager_audit_db";

    @Override
    public void logApiCall(String username, String endpoint, String method, String ip,
            String userAgent, String sourceType, int statusCode, String createdBy) {

        String sql = "INSERT INTO " + AUDIT_DB + ".api_audit_log " +
                "(username, endpoint, method, ip_address, user_agent, source_type, status_code, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String url = "jdbc:mariadb://" + dbHost + ":" + dbPort + "/" + AUDIT_DB;

        try (
                Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, endpoint);
            stmt.setString(3, method);
            stmt.setString(4, ip);
            stmt.setString(5, userAgent);
            stmt.setString(6, sourceType);
            stmt.setInt(7, statusCode);
            stmt.setString(8, createdBy);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
