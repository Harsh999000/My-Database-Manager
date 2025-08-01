package com.harsh.my_database_manager.dto;

public class SignupRequest {

    private String username;
    private String password;
    private String virtualDbName;

    // Constructors
    public SignupRequest() {
    }

    public SignupRequest(String username, String password, String virtualDbName) {
        this.username = username;
        this.password = password;
        this.virtualDbName = virtualDbName;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVirtualDbName() {
        return virtualDbName;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVirtualDbName(String virtualDbName) {
        this.virtualDbName = virtualDbName;
    }
}
