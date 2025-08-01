package com.harsh.my_database_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Service
public class AuditLoggerService {

    @Autowired
    private DataSource dataSource;

    // #region - Start - Login Audit Logger
    public void logLoginAttempt(
            String username,
            String status, // "success" or "failure"
            String reason, // e.g. "wrong password", "user inactive"
            String ipAddress,
            String userAgent,
            String tokenIssued, // nullable, if success
            int statusCode // 200, 401, etc.
    ) {
        String sql = "INSERT INTO dbmanager_audit_db.login_audit_log " +
                "(username, status, reason, ip_address, user_agent, token_issued, status_code, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, status);
            stmt.setString(3, reason);
            stmt.setString(4, ipAddress);
            stmt.setString(5, userAgent);
            stmt.setString(6, tokenIssued); // if null, JDBC will handle
            stmt.setInt(7, statusCode);
            stmt.setString(8, "system");

            stmt.executeUpdate();

        } catch (Exception e) {
            // Don't throw error back — audit logging should not affect main login flow
            e.printStackTrace();
        }
    }
    // #endregion - End - Login Audit Logger

    // #region - Start - Signup Audit Logger
    public void logSignupAttempt(
            String username,
            String status, // "success" or "failure"
            String reason,
            String ipAddress,
            String userAgent,
            int statusCode) {
        String sql = "INSERT INTO dbmanager_audit_db.signup_audit_log " +
                "(username, status, reason, ip_address, user_agent, status_code, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, status);
            stmt.setString(3, reason);
            stmt.setString(4, ipAddress);
            stmt.setString(5, userAgent);
            stmt.setInt(6, statusCode);
            stmt.setString(7, "system");

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(); // keep failure silent, never block signup
        }
    }
    // #endregion - End - Signup Audit Logger
}
