package com.identity.service;

import com.identity.dto.PasswordResetRequest;
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


    public String sendOtpForReset(String username) {
        log.info("[AuthService] sendOtpForReset for username: {}", username);

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        // ✅ Find user by username
        Optional<UserCredential> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            log.warn("[AuthService] User not found for username: {}", username);
            throw new IllegalArgumentException("User not found with given username");
        }

        UserCredential user = userOpt.get();

        // ✅ Check if email exists
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.warn("[AuthService] No email found for username: {}", username);
            throw new IllegalArgumentException("No email associated with this username");
        }

        // ✅ Generate OTP
        String otp = otpService.generateOtp(user.getEmail());
//        String otp = "123456";

        // ✅ Send OTP to user's email
        emailService.sendOtpEmail(user.getEmail(), otp);

        log.info("[AuthService] OTP sent successfully to {}", user.getEmail());
        return "OTP sent successfully to " + user.getEmail();
    }

    public String resetPassword(PasswordResetRequest dto) {
        log.info("[AuthService] resetPassword for username: {}", dto.getUserName());

        // ✅ Step 1: Validate input
        if (dto.getUserName() == null || dto.getUserName().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (dto.getNewPassword() == null || dto.getConfirmPassword() == null) {
            throw new IllegalArgumentException("Both passwords are required");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // ✅ Step 2: Find the user by username
        Optional<UserCredential> userOpt = userRepository.findByUsername(dto.getUserName());
        if (userOpt.isEmpty()) {
            log.warn("[AuthService] User not found for username: {}", dto.getUserName());
            throw new IllegalArgumentException("User not found");
        }

        UserCredential user = userOpt.get();

        // ✅ Step 3: Check for type (e.g., "SA")
        if (dto.getType() != null && dto.getType().equalsIgnoreCase("SA")) {
            user.setSelfAuthentication(false);
            log.info("[AuthService] Type is SA — selfAuthentication set to false for user: {}", user.getUsername());
        }

        if (dto.getType() != null && dto.getType().equalsIgnoreCase("otp")) {
            user.setSelfAuthentication(false);
            user.setOtpAuthentication(false);
            log.info("[AuthService] Type is SA — selfAuthentication set to false for user: {}", user.getUsername());
        }


        // ✅ Step 3: Encode and update the new password
        String hashedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPasswordHash(hashedPassword);
        user.setLastModifyDate(LocalDateTime.now());
        user.setSelfAuthentication(false);
        userRepository.save(user);

        // ✅ Step 4: Send confirmation email (only if user has an email)
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            emailService.sendPasswordResetSuccessEmail(user.getEmail(), user.getUsername());
        }

        log.info("[AuthService] Password reset successfully for username: {}", dto.getUserName());
        return "Password reset successfully. A confirmation email has been sent "+user.getEmail();
    }
}
