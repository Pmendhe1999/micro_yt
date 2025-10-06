package com.identity.service;

import com.identity.entity.UserCredential;
import com.identity.reository.UserCredentialRepository;
import com.identity.reository.UserRepository;
import com.identity.serviceImpl.EmailService;
import com.identity.serviceImpl.UserServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private  OtpService otpService;

    @Autowired
    private EmailService emailService;


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);



    public String saveUser(UserCredential credential, String token) {
        if (credential.getPasswordHash() == null || credential.getPasswordHash().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // ✅ Extract details from token
        String createdByUser = jwtService.extractUsername(token); // "sub"
        String role = jwtService.extractRole(token);              // "roles"

        // ✅ Encode password
        credential.setPasswordHash(passwordEncoder.encode(credential.getPasswordHash()));



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


    public String sendOtpForReset(String email, String username) {
        log.info("[AuthService] sendOtpForReset for email: {}", email);

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        Optional<UserCredential> userOpt;

        if (username != null && !username.isEmpty()) {
            userOpt = userRepository.findByUsername(username);
        } else {
            userOpt = userRepository.findByEmail(email);
        }

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with given email or username");
        }

        // ✅ Generate and store OTP
        String otp = otpService.generateOtp(email);

        // ✅ Send OTP via email
        emailService.sendOtpEmail(email, otp);

        log.info("[AuthService] OTP sent successfully to {}", email);
        return "OTP sent successfully. Check your email.";
    }

}
