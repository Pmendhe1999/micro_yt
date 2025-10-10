package com.identity.controller;


import com.identity.config.CustomUserDetails;
import com.identity.dto.AuthRequest;
import com.identity.dto.ChangePasswordRequest;
import com.identity.dto.OtpRequestDTO;
import com.identity.dto.PasswordResetRequest;
import com.identity.entity.UserCredential;
import com.identity.service.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService; // ✅ Make sure you have a JwtService bean

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCredential user,
                                          BindingResult bindingResult,
                                          @RequestHeader("Authorization") String authHeader) {
        try {
            // 1. Validate request body
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errors);
            }

            // 2. Extract JWT token (remove "Bearer ")
            String token = authHeader.replace("Bearer ", "");

            // 3. Save user with audit fields
            String response = service.saveUser(user, token);
            return ResponseEntity.ok(Collections.singletonMap("message", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Collections.singletonMap("error", "Invalid user data: " + e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error",
                            "An unexpected error occurred while registering user: " + e.getMessage()));
        }
    }

    @GetMapping("/getString")
    public String getString(){
        return "you hit the api";
    }

    @PostMapping("/token")
    public ResponseEntity<?>getToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            if (authenticate.isAuthenticated()) {

                // ✅ Load user details
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(authRequest.getUsername());
// ✅ Generate JWT token with full UserDetails
                String token = jwtService.generateToken(userDetails);

                // ✅ Prepare response
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("userId", userDetails.getUserId());
                response.put("firstName", userDetails.getFirstName());
                response.put("lastName", userDetails.getLastName());
                response.put("selfAuthentication", userDetails.getSelfAuthentication());

                // ✅ Include Base Image URL if available
                if (userDetails.getUser().getMediaDetails() != null
                        && userDetails.getUser().getMediaDetails().getMedia() != null) {
                    response.put("baseImageUrl", userDetails.getUser()
                            .getMediaDetails()
                            .getMedia()
                            .getBaseImageUrl());
                } else {
                    response.put("baseImageUrl", null);
                }
                response.put("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());

                return ResponseEntity.ok(response);

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access");
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while generating token");
        }
    }

//    @GetMapping("/validate")
//    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
//        try {
//            service.validateToken(token);
//            return ResponseEntity.ok("Token is valid");
//        } catch (ExpiredJwtException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
//        } catch (MalformedJwtException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An unexpected error occurred while validating token");
//        }
//    }


    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestParam String token) {
        jwtService.validateToken(token);
        String username = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String username = request.get("userName");

        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username is required"));
        }

        try {
            String message = service.sendOtpForReset(username);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            // ✅ Custom application error (bad request)
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // ✅ Unexpected error
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequestDTO dto) {
        String message = otpService.verifyOtp(dto.getUserName(), dto.getOtp());

        if ("OTP verified successfully".equalsIgnoreCase(message)) {
            return ResponseEntity.ok(Map.of("message", message));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest dto) {
        try {
            String message = service.resetPassword(dto);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }


}
