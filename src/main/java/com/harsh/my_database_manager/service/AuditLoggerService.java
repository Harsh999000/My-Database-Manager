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

    // #region - Start - Session Audit Logger
    // #region - Start - Session Audit Logger for Login Events
    public void logSessionLogin(
            String username,
            String tokenHash,
            String ipAddress,
            String userAgent,
            String createdBy) {
        String sql = "INSERT INTO dbmanager_audit_db.session_audit_log " +
                "(username, token_hash, status, ip_address, user_agent, login_time, logout_reason, logout_time, created_by) "
                +
                "VALUES (?, ?, 'active', ?, ?, NOW(), '-', NULL, ?)";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, tokenHash);
            stmt.setString(3, ipAddress);
            stmt.setString(4, userAgent);
            stmt.setString(5, createdBy);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(); // Optional: log this
        }
    }
    // #endregion - End - Session Audit Logger for Login Events

    // #region - Start - Session Audit Logger for Logout Events
    public int logSessionLogout(
            String username,
            String tokenHash,
            String reason,
            String ipAddress,
            String userAgent,
            String updatedBy) {
        String sql = "UPDATE dbmanager_audit_db.session_audit_log " +
                "SET status = 'logged_out', logout_reason = ?, " +
                "ip_address = ?, user_agent = ?, logout_time = NOW(), " +
                "created_by = ? " +
                "WHERE username = ? AND token_hash = ? AND status = 'active'";

        int updatedRows = 0;

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reason);
            stmt.setString(2, ipAddress);
            stmt.setString(3, userAgent);
            stmt.setString(4, updatedBy);
            stmt.setString(5, username);
            stmt.setString(6, tokenHash);

            updatedRows = stmt.executeUpdate(); // store result inside try block
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updatedRows;
    }
    // #endregion - End - Session Audit Logger for Logout Events
    // #endregion - End - Session Audit Logger

}
