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


    public String saveUser(UserCredential credential) {
        try {
            if (credential.getPassword() == null || credential.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }

            credential.setPassword(passwordEncoder.encode(credential.getPassword()));
            repository.save(credential);

            return "User added to the system";
        } catch (IllegalArgumentException e) {
            throw e; // Let controller/global handler manage this
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user", e);
        }
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
