package com.identity.config;

import com.identity.entity.UserCredential;
import com.identity.reository.UserCredentialRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordMigration {

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void migratePasswords() {
        List<UserCredential> users = repository.findAll();
        for (UserCredential user : users) {
            String pwd = user.getPasswordHash();
            // BCrypt hashes start with $2a$, $2b$, etc.
            if (!pwd.startsWith("$2a$") && !pwd.startsWith("$2b$")) {
                user.setPasswordHash(passwordEncoder.encode(pwd));
                repository.save(user);
                System.out.println("Updated password for user: " + user.getUsername());
            }
        }
    }
}
