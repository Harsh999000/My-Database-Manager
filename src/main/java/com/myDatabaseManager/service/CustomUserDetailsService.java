package com.myDatabaseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // SQL query to fetch user info
        String sql = "SELECT username, password_hash, role FROM mydbmanager_user.user_details WHERE username = ?";

        // Query the database
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, username);

        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Fetch the first row (there should be only one match)
        Map<String, Object> row = users.get(0);

        String dbUsername = (String) row.get("username");
        String passwordHash = (String) row.get("password_hash");
        String role = (String) row.get("role");

        // Create a list of authorities (Spring Security requires roles to be prefixed
        // with "ROLE_")
        List<GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

        // Return a Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(dbUsername, passwordHash, authorities);
    }
}
