package com.harsh.my_database_manager.controller;

import com.harsh.my_database_manager.dto.LoginRequest;
import com.harsh.my_database_manager.dto.SignupRequest;
import com.harsh.my_database_manager.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // #region - Start - Login Authorization
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        return authService.login(request, servletRequest);
    }
    // #endregion - End - Login Authorization

    // #region - Start - Signup Authorization
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request, HttpServletRequest servletRequest) {
        return authService.signup(request, servletRequest);
    }
    // #endregion - End - Signup Authorization

    // #region - Start - Logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Map<String, Object> response = authService.logout(request);
        if ((boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    // #endregion - End - Logut

}
