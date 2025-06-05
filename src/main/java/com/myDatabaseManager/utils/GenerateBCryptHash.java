package com.myDatabaseManager.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBCryptHash {

    public static String generateHash(String plainPassword) {
        return new BCryptPasswordEncoder().encode(plainPassword);
    }

    public static void main(String[] args) {
        String password = "Harsh0@server";
        String hashed = generateHash(password);
        // Use this method to get the hash in code if needed in future
    }
}
