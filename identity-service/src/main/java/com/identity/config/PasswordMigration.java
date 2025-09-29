package com.identity.config;

import com.identity.entity.UserCredential;
import com.identity.reository.UserCredentialRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        List<UserCredential> toUpdate = new ArrayList<>();
        for (UserCredential user : users) {
            String pwd = user.getPasswordHash();
            if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$")) {
                user.setPasswordHash(passwordEncoder.encode(pwd));
                toUpdate.add(user);  // collect updated users
            }
        }

        repository.saveAll(toUpdate);  // batch save outside the loop
        System.out.println("Updated " + toUpdate.size() + " users");
    }
}
