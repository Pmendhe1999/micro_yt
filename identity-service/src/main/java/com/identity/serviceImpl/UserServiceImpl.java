package com.identity.serviceImpl;

import com.identity.dto.UserRegisterDto;
import com.identity.entity.Application;
import com.identity.entity.Authority;
import com.identity.entity.UserCredential;
import com.identity.reository.ApplicationRepository;
import com.identity.reository.AuthorityRepository;
import com.identity.reository.UserRepository;
import com.identity.service.JwtService;
import com.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {

        private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

        @Autowired
        private  UserRepository repository;

        @Autowired
        private  PasswordEncoder passwordEncoder;

        @Autowired
        private  JwtService jwtService;
        @Autowired
        private AuthorityRepository authorityRepository;

        @Autowired
        private ApplicationRepository applicationRepository;

        // CREATE
        @Override
        public UserCredential saveUser(UserRegisterDto dto, String token) {
            try {
                if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
                    log.warn("Password missing while creating User: {}", dto.getUsername());
                    throw new IllegalArgumentException("Password cannot be null or empty");
                }

                if (repository.existsByEmail(dto.getEmail())) {
                    throw new IllegalArgumentException("Email already exists");
                }

                if (repository.existsByUsername(dto.getUsername())) {
                    throw new IllegalArgumentException("Username already exists");
                }

                String createdByUser = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);

                UserCredential credential = new UserCredential();
                credential.setUsername(dto.getUsername());
                credential.setEmail(dto.getEmail());
                credential.setPhone(dto.getPhone());
                credential.setPassword(passwordEncoder.encode(dto.getPassword()));

                if (dto.getStatus() != null) {
                    credential.setStatus(UserCredential.Status.valueOf(dto.getStatus().toUpperCase()));
                } else {
                    credential.setStatus(UserCredential.Status.PENDING);
                }

                credential.setCreatedAt(LocalDateTime.now());

                Application app = applicationRepository.findById(dto.getApplicationId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Application ID"));
                credential.setApplication(app);

                if (dto.getAuthorities() != null && !dto.getAuthorities().isEmpty()) {
                    Set<Authority> authorities = new HashSet<>(authorityRepository.findAllById(dto.getAuthorities()));
                    credential.setAuthorities(authorities);
                }

                UserCredential saved = repository.save(credential);
                log.info("User '{}' created by {} (role={})", saved.getUsername(), createdByUser, role);

                return saved;

            } catch (Exception e) {
                log.error("Error while creating User: {}", e.getMessage(), e);
                throw e;
            }
        }

        // READ ALL (with search & pagination)
        @Override
        public Page<UserCredential> getAllUsers(String search, Pageable pageable) {
            try {
                if (search != null && !search.isEmpty()) {
                    log.debug("Fetching Users with search filter: {}", search);
                    return repository.findAll(pageable); // 🔹 Replace with custom search if needed
                }
                log.debug("Fetching all Users");
                return repository.findAll(pageable);
            } catch (Exception e) {
                log.error("Error while fetching Users: {}", e.getMessage(), e);
                throw e;
            }
        }

        // READ BY ID
        @Override
        public Optional<UserCredential> getUserById(Long id) {
            try {
                log.debug("Fetching User by id={}", id);
                return repository.findByUserId(id);
            } catch (Exception e) {
                log.error("Error while fetching User id={}: {}", id, e.getMessage(), e);
                throw e;
            }
        }

        // UPDATE
        @Override
        public UserCredential updateUser(Long id, UserRegisterDto dto, String token) {
            try {
                UserCredential existing = repository.findByUserId(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));

                String modifiedByUser = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);

                existing.setUsername(dto.getUsername());
                existing.setEmail(dto.getEmail());
                existing.setPhone(dto.getPhone());

                if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                    existing.setPassword(passwordEncoder.encode(dto.getPassword()));
                }

                if (dto.getStatus() != null) {
                    existing.setStatus(UserCredential.Status.valueOf(dto.getStatus().toUpperCase()));
                }

                if (dto.getApplicationId() != null) {
                    Application app = applicationRepository.findById(dto.getApplicationId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Application ID"));
                    existing.setApplication(app);
                }

                if (dto.getAuthorities() != null) {
                    Set<Authority> authorities = dto.getAuthorities().stream()
                            .map(authId -> authorityRepository.findById(authId)
                                    .orElseThrow(() -> new NoSuchElementException("Authority not found with id " + authId)))
                            .collect(Collectors.toSet());
                    existing.setAuthorities(authorities);
                }

                UserCredential updated = repository.save(existing);
                log.info("User id={} updated by {} (role={})", updated.getUserId(), modifiedByUser, role);

                return updated;

            } catch (Exception e) {
                log.error("Error while updating User id={}: {}", id, e.getMessage(), e);
                throw e;
            }
        }

        // DELETE
        @Override
        public UserCredential deleteUser(Long id, String token) {
            try {
                UserCredential existing = repository.findByUserId(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));

                String deletedByUser = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);

                repository.delete(existing);

                log.info("User id={} deleted by {} (role={})", id, deletedByUser, role);
                return existing;

            } catch (Exception e) {
                log.error("Error while deleting User id={}: {}", id, e.getMessage(), e);
                throw e;
            }
        }
}
