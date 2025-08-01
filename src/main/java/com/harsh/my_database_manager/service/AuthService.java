package com.harsh.my_database_manager.service;

import com.harsh.my_database_manager.dto.SignupRequest;
import com.harsh.my_database_manager.dto.LoginRequest;
import com.harsh.my_database_manager.util.JwtUtil;
import com.harsh.my_database_manager.util.TokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

        @Autowired
        private UserService userService;

        @Autowired
        private DataSource dataSource;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private AuditLoggerService auditLoggerService;

        private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // #region - Start - Login Auth
        public ResponseEntity<?> login(LoginRequest request, HttpServletRequest servletRequest) {
                String username = request.getUsername();
                String password = request.getPassword();

                String ip = servletRequest.getRemoteAddr();
                String userAgent = servletRequest.getHeader("User-Agent");

                String sql = "SELECT us.password_hash, ud.role " +
                                "FROM dbmanager_user_secret_db.user_secret us " +
                                "JOIN dbmanager_user_db.user_details ud ON us.username = ud.username " +
                                "WHERE us.username = ? AND ud.status = 'active'";

                try (
                                Connection conn = dataSource.getConnection();
                                PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, username);
                        ResultSet rs = stmt.executeQuery();

                        if (!rs.next()) {
                                auditLoggerService.logLoginAttempt(username, "failure", "user not found or inactive",
                                                ip, userAgent, null, 401);
                                return ResponseEntity.status(401)
                                                .body("{\"success\": false, \"message\": \"Invalid credentials.\"}");
                        }

                        String storedHash = rs.getString("password_hash");
                        String role = rs.getString("role");

                        if (!passwordEncoder.matches(password, storedHash)) {
                                auditLoggerService.logLoginAttempt(username, "failure", "wrong password",
                                                ip, userAgent, null, 401);
                                return ResponseEntity.status(401)
                                                .body("{\"success\": false, \"message\": \"Invalid credentials.\"}");
                        }

                        // Generate JWT Token
                        String token = jwtUtil.generateToken(username, role);

                        // #region - Start - Session Audit Logging for Login
                        String tokenHash = TokenUtil.sha256(token);
                        auditLoggerService.logSessionLogin(
                                        username,
                                        tokenHash,
                                        ip,
                                        userAgent,
                                        username);
                        // #endregion - End - Session Audit Logging for Login

                        auditLoggerService.logLoginAttempt(username, "success", "login success",
                                        ip, userAgent, token, 200);

                        String jsonResponse = String.format(
                                        "{\"success\": true, \"message\": \"Login successful.\", \"token\": \"%s\"}",
                                        token);

                        return ResponseEntity.ok().body(jsonResponse);

                } catch (Exception e) {
                        e.printStackTrace();
                        auditLoggerService.logLoginAttempt(username, "failure", "internal server error",
                                        ip, userAgent, null, 500);
                        return ResponseEntity.internalServerError()
                                        .body("{\"success\": false, \"message\": \"Server error during login.\"}");
                }
        }
        // #endregion - End - Login Auth

        // #region - Start - Signup Auth
        public ResponseEntity<?> signup(SignupRequest request, HttpServletRequest servletRequest) {
                String username = request.getUsername();
                String password = request.getPassword();
                String virtualDbName = request.getVirtualDbName();

                String ip = servletRequest.getRemoteAddr();
                String userAgent = servletRequest.getHeader("User-Agent");

                if (userService.usernameExists(username)) {
                        auditLoggerService.logSignupAttempt(username, "failure", "username already exists",
                                        ip, userAgent, 400);
                        return ResponseEntity.badRequest()
                                        .body("{\"success\": false, \"message\": \"Username already exists.\"}");
                }

                if (userService.virtualDbExists(username, virtualDbName)) {
                        auditLoggerService.logSignupAttempt(username, "failure", "virtual DB already exists",
                                        ip, userAgent, 400);
                        return ResponseEntity.badRequest()
                                        .body("{\"success\": false, \"message\": \"Virtual DB already exists for this user.\"}");
                }

                String hashedPassword = passwordEncoder.encode(password);

                try (Connection conn = dataSource.getConnection()) {
                        conn.setAutoCommit(false); // Start transaction

                        // user_details
                        String insertUserDetails = "INSERT INTO dbmanager_user_db.user_details " +
                                        "(username, status, role, created_by, last_updated_by, deleted_by) VALUES (?, 'active', 'admin', 'signup', 'signup', '-')";
                        try (PreparedStatement stmt = conn.prepareStatement(insertUserDetails)) {
                                stmt.setString(1, username);
                                stmt.executeUpdate();
                        }

                        // user_secret
                        String insertUserSecret = "INSERT INTO dbmanager_user_secret_db.user_secret (username, password_hash) VALUES (?, ?)";
                        try (PreparedStatement stmt = conn.prepareStatement(insertUserSecret)) {
                                stmt.setString(1, username);
                                stmt.setString(2, hashedPassword);
                                stmt.executeUpdate();
                        }

                        // virtual_db_map
                        String insertVirtualDb = "INSERT INTO dbmanager_access_db.virtual_db_map " +
                                        "(virtual_db_name, username, created_by, last_updated_by, deleted_by, status) "
                                        +
                                        "VALUES (?, ?, 'signup', 'signup', '-', 'active')";

                        try (PreparedStatement stmt = conn.prepareStatement(insertVirtualDb)) {
                                stmt.setString(1, virtualDbName);
                                stmt.setString(2, username);
                                stmt.executeUpdate();
                        }

                        conn.commit();

                        auditLoggerService.logSignupAttempt(username, "success", "signup success",
                                        ip, userAgent, 200);

                        return ResponseEntity
                                        .ok("{\"success\": true, \"message\": \"Signup successful. Please log in.\"}");

                } catch (Exception e) {
                        e.printStackTrace();
                        auditLoggerService.logSignupAttempt(username, "failure", "internal server error",
                                        ip, userAgent, 500);

                        return ResponseEntity.internalServerError()
                                        .body("{\"success\": false, \"message\": \"Signup failed due to a server error.\"}");
                }
        }
        // #endregion - End - Signup Auth

        // #region - Start - Logout Method with Session Audit Logging
        public Map<String, Object> logout(HttpServletRequest request) {
                Map<String, Object> response = new HashMap<>();
                try {
                        String authHeader = request.getHeader("Authorization");
                        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                                response.put("success", false);
                                response.put("error", "Missing or invalid Authorization header");
                                return response;
                        }

                        String token = authHeader.substring(7);
                        String username = jwtUtil.getUsernameFromToken(token);
                        String tokenHash = TokenUtil.sha256(token);

                        String ipAddress = request.getRemoteAddr();
                        String userAgent = request.getHeader("User-Agent");

                        int result = auditLoggerService.logSessionLogout(
                                        username,
                                        tokenHash,
                                        "manual",
                                        ipAddress,
                                        userAgent,
                                        username);

                        if (result == 0) {
                                response.put("success", false);
                                response.put("error", "Session already logged out or not found.");
                        } else {
                                response.put("success", true);
                                response.put("message", "User logged out successfully");
                        }

                } catch (Exception e) {
                        response.put("success", false);
                        response.put("error", "Logout failed: " + e.getMessage());
                }
                return response;
        }
        // #endregion - End - Logout Method with Session Audit Logging

}
