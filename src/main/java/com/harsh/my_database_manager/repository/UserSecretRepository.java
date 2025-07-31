package com.harsh.my_database_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.harsh.my_database_manager.entity.UserSecret;

public interface UserSecretRepository extends JpaRepository<UserSecret, String> {
    // Fetch the hashed password by username
    UserSecret findByUsername(String username);
}