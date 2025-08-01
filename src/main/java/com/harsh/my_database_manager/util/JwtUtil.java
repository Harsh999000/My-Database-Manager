package com.harsh.my_database_manager.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;

@Component

public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyRaw;

    @Value("${jwt.expiration}")
    private long expirationMillis;

    private Key key;

    @PostConstruct
    public void init() {
        // Convert raw secret string into a Key object
        this.key = Keys.hmacShaKeyFor(secretKeyRaw.getBytes());
    }

    // Generate JWT token with username and role
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Internal method to extract claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate token's signature and expiration
    public boolean validateToken(String token, String expectedUsername) {
        try {
            Claims claims = extractAllClaims(token);
            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            return (username.equals(expectedUsername) && !expiration.before(new Date()));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract username (subject) from token
    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract role (custom claim) from token
    public String getRoleFromToken(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
