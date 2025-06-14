package com.capstone.warehousesvc.security;

import com.capstone.warehousesvc.enums.UserRole;
import com.capstone.warehousesvc.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // For JWT-based authentication, we extract user details from the token
            // In a real implementation, you might want to validate with the login service
            // that the user still exists and is active

            log.info("Creating AuthUser for username: {}", username);

            // Create a basic AuthUser with information from the JWT claims
            return AuthUser.builder()
                    .id(UUID.randomUUID().toString()) // This would come from the JWT in a real implementation
                    .email(username)
                    .password("") // Password not needed for token-based auth
                    .name("User from JWT")
                    .role(UserRole.STAFF) // Default role, would come from JWT in real implementation
                    .build();
        } catch (Exception e) {
            log.error("Error creating AuthUser: {}", e.getMessage());
            throw new NotFoundException("User not found or token invalid: " + e.getMessage());
        }
    }
}
