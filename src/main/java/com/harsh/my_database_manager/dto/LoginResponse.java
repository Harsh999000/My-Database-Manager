package com.harsh.my_database_manager.dto;

// DTO for Login Response Data

public class LoginResponse {
    private String username;
    private String role;
    private String jwtToken;

    // Default constructor
    public LoginResponse() {
    }

    // Parameterized constructor
    public LoginResponse(String username, String role, String jwtToken) {
        this.username = username;
        this.role = role;
        this.jwtToken = jwtToken;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
