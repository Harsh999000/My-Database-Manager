package com.harsh.my_database_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.harsh.my_database_manager.entity.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    // Fetch a user by username
    UserDetails findByUsername(String username);

    // Check if a user exists with the given username
    boolean existsByUsername(String username);
}
