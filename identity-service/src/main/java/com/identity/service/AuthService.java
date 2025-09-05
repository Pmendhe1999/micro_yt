package com.identity.service;

import com.identity.entity.UserCredential;
import com.identity.reository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "user added to the system";
    }

    public String generateToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.generateToken(userDetails);
    }


    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
