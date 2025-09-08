package com.identity.service;

import com.identity.entity.UserCredential;
import com.identity.reository.UserCredentialRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;


    public String saveUser(UserCredential credential, String token) {
        if (credential.getPassword() == null || credential.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // ✅ Extract details from token
        String createdByUser = jwtService.extractUsername(token); // "sub"
        String role = jwtService.extractRole(token);              // "roles"

        // ✅ Encode password
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));

        // ✅ Set audit fields
        credential.setCreatedBy(createdByUser + " (" + role + ")");
        credential.setCreatedDate(LocalDateTime.now());
        credential.setLastModifiedBy(createdByUser + " (" + role + ")");
        credential.setLastModifiedDate(LocalDateTime.now());

        repository.save(credential);

        return "User registered successfully by " + createdByUser + " with role " + role;
    }

    public String generateToken(String username) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.generateToken(userDetails);
        } catch (UsernameNotFoundException e) {
            throw e; // handled by controller/global handler
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token for user: " + username, e);
        }
    }


    public void validateToken(String token) {
        try {
            jwtService.validateToken(token);
        } catch (ExpiredJwtException | MalformedJwtException e) {
            throw e; // bubble up for controller/global handler
        } catch (Exception e) {
            throw new RuntimeException("Token validation failed", e);
        }
    }

}
