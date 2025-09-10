package com.identity.serviceImpl;

import com.identity.dto.UserRegisterDto;
import com.identity.entity.Authority;
import com.identity.entity.UserCredential;
import com.identity.reository.AuthorityRepository;
import com.identity.reository.UserRepository;
import com.identity.service.JwtService;
import com.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository repository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private  JwtService jwtService;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public String saveUser(UserRegisterDto dto, String token) {
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String createdByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        UserCredential credential = new UserCredential();
        credential.setName(dto.getName());
        credential.setEmail(dto.getEmail());
        credential.setPassword(passwordEncoder.encode(dto.getPassword()));
        credential.setRole(dto.getRole());
        credential.setCreatedBy(createdByUser + " (" + role + ")");
        credential.setCreatedDate(LocalDateTime.now());
        credential.setLastModifiedBy(createdByUser + " (" + role + ")");
        credential.setLastModifiedDate(LocalDateTime.now());

        // Attach authorities
        if (dto.getAuthorities() != null && !dto.getAuthorities().isEmpty()) {
            Set<Authority> authorities = new HashSet<>(
                    authorityRepository.findAllById(dto.getAuthorities())
            );
            credential.setAuthorities(authorities);
        }

        repository.save(credential);

        return "User registered successfully by " + createdByUser + " with role " + role;
    }
    // READ ALL
    @Override
    public List<UserCredential> getAllUsers() {
        return repository.findAll();
    }

    // READ BY ID
    @Override
    public Optional<UserCredential> getUserById(int id) {
        return repository.findById(id);
    }

    // UPDATE
    @Override
    public String updateUser(int id, UserRegisterDto dto, String token) {
        UserCredential existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));

        String modifiedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getAuthorities() != null) {
            Set<Authority> authorities = dto.getAuthorities().stream()
                    .map(authId -> authorityRepository.findById(authId)
                            .orElseThrow(() -> new NoSuchElementException("Authority not found with id " + authId)))
                    .collect(Collectors.toSet());
            existing.setAuthorities(authorities);
        }

        existing.setLastModifiedBy(modifiedByUser + " (" + role + ")");
        existing.setLastModifiedDate(LocalDateTime.now());

        repository.save(existing);
        return "User updated successfully by " + modifiedByUser + " with role " + role;
    }

    // DELETE
    @Override
    public String deleteUser(int id, String token) {
        try {
            UserCredential existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            return "User deleted successfully by " + deletedByUser + " with role " + role;

        } catch (NoSuchElementException e) {
            throw e; // handled at controller
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting user: " + e.getMessage(), e);
        }
    }

}
