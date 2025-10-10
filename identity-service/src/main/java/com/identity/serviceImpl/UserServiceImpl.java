package com.identity.serviceImpl;

import com.identity.dto.ChangePasswordRequest;
import com.identity.dto.UserRegisterDto;
import com.identity.entity.*;
import com.identity.reository.*;
import com.identity.service.JwtService;
import com.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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

        @Autowired
        private EmailService emailService;

        @Autowired
        private  AppFunTypesMasterRepository appFunTypesMasterRepository;

        @Autowired
        private AppFunctionRepository appFunctionRepository;

        @Autowired
        private AuthTypeRepository authTypeRepository;

        private AuthTypeMasterRepository authTypeMasterRepository;

        // CREATE
        @Override
        public UserCredential saveUser(UserRegisterDto dto) {
            try {
                String generatedPassword = generateRandomPassword();
                dto.setPassword(generatedPassword);
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

                UserCredential credential = new UserCredential();
                credential.setUsername(dto.getUsername());

                credential.setEmail(dto.getEmail());
                credential.setCountry(dto.getCountry());
                credential.setMobileNumber(dto.getMobileNumber());
                credential.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

                // ✅ required fields from payload
                credential.setFirstName(dto.getFirstName());
                credential.setLastName(dto.getLastName());

                // ✅ defaults for others
                credential.setActivated(true);
                credential.setAuthStatus(true);
                credential.setStatus(UserCredential.Status.ACTIVE);
                credential.setCreatedDate(LocalDateTime.now());
                credential.setLastModifyDate(LocalDateTime.now());
                credential.setResetDate(null); // only when reset requested

                // Map application only if provided
                if (dto.getApplicationIds() != null && !dto.getApplicationIds().isEmpty()) {
                    Set<Application> applications = new HashSet<>(applicationRepository.findAllById(dto.getApplicationIds()));
                    if (applications.isEmpty()) {
                        throw new IllegalArgumentException("Invalid Application IDs");
                    }
                    credential.setApplications(applications);
                }
                if (dto.getAuthorities() != null && !dto.getAuthorities().isEmpty()) {
                    Set<Authority> authorities = new HashSet<>(authorityRepository.findAllById(dto.getAuthorities()));
                    credential.setAuthorities(authorities);
                }

                // 🔹 Get the first application from DTO
                Long appId = null;
                if (dto.getApplicationIds() != null && !dto.getApplicationIds().isEmpty()) {
                    appId = dto.getApplicationIds().iterator().next();
                }

                Application application = null;
                if (appId != null) {
                    application = applicationRepository.findById(appId).orElse(null);
                }

                // 🔹 Get AppFunTypesMaster with name "Registration"
                AppFunTypesMaster regFunType = appFunTypesMasterRepository.findByNameIgnoreCase("Registration").orElse(null);

                // 🔹 Find AppFunction by AppFunTypesMaster + Application
                AppFunction appFunction = null;
                if (regFunType != null && application != null) {
                    appFunction = appFunctionRepository.findByAppFunTypesMasterAndApplication(regFunType, application).orElse(null);
                }

                // 🔹 Find AuthType by AppFunction
                AuthType authType = null;
                if (appFunction != null) {
                    authType = authTypeRepository.findByAppFunction(appFunction).orElse(null);
                }

                // 🔹 Retrieve AuthTypeMaster — prefer "self-authentication" if available
                AuthTypeMaster authTypeMaster = null;
                if (authType != null) {
                    authTypeMaster = authType.getAuthTypeMaster();
                }

                // If not found from AuthType, find explicitly by name = "self-authentication"
                if (authTypeMaster == null) {
                    authTypeMaster = authTypeMasterRepository.findByNameIgnoreCase("self-authentication").orElse(null);
                }
                // ✅ Set selfAuthentication flag based on auth type
                if (authTypeMaster != null &&
                        authTypeMaster.getName().equalsIgnoreCase("self-authentication")) {
                    credential.setSelfAuthentication(true);
                } else {
                    credential.setSelfAuthentication(false);
                }

                UserCredential saved = repository.save(credential);
                log.info("User '{}' created successfully", saved.getUsername());

                // 9️⃣ Send email only if AuthTypeMaster name = email_notification
                if ("email_notification".equalsIgnoreCase(authTypeMaster.getName())) {
                    String loginUrl = "http://yourdomain.com/login";
                    emailService.sendCredentialsEmail(
                            saved.getEmail(),
                            saved.getUsername(),
                            credential.getPasswordHash(), // Use plain password if needed (DTO should carry it)
                            loginUrl
                    );
                    log.info("Credentials email sent to '{}'", saved.getEmail());
                } else {
                    log.info("AuthTypeMaster is '{}', skipping email notification", authTypeMaster.getName());
                }

                String loginUrl = "http://yourdomain.com/login"; // put your actual login URL
                emailService.sendCredentialsEmail(saved.getEmail(), saved.getUsername(), dto.getPassword(), loginUrl);

                return saved;

            } catch (Exception e) {
                log.error("Error while creating User: {}", e.getMessage(), e);
                throw e;
            }
        }
        private String generateRandomPassword() {
            int length = 10;
            String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String lowerCase = "abcdefghijklmnopqrstuvwxyz";
            String digits = "0123456789";
            String specialChars = "@#$%&*!";
            String allChars = upperCase + lowerCase + digits + specialChars;

            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder();

            // Ensure password has at least one of each type
            password.append(upperCase.charAt(random.nextInt(upperCase.length())));
            password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
            password.append(digits.charAt(random.nextInt(digits.length())));
            password.append(specialChars.charAt(random.nextInt(specialChars.length())));

            // Fill remaining characters randomly
            for (int i = 4; i < length; i++) {
                password.append(allChars.charAt(random.nextInt(allChars.length())));
            }

            // Shuffle for randomness
            List<Character> chars = password.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toList());
            Collections.shuffle(chars);
            return chars.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        }



//        // READ ALL (with search & pagination)
//        @Override
//        public Page<UserCredential> getAllUsers(String search, Pageable pageable) {
//            try {
//                if (search != null && !search.isEmpty()) {
//                    log.debug("Fetching Users with search filter: {}", search);
//                    return repository.searchUsers(search, pageable); // 🔹 Replace with custom search if needed
//                }
//                log.debug("Fetching all Users");
//                return repository.findAll(pageable);
//            } catch (Exception e) {
//                log.error("Error while fetching Users: {}", e.getMessage(), e);
//                throw e;
//            }
//        }

        @Override
        public Page<UserCredential> getAllUsersWithFilters(
                String username,
                String email,
                String mobileNumber,
                String country,
                String firstName,
                String lastName,
                List<Long> applicationIds,
                List<Long> authorityIds,
                Pageable pageable) {

            return repository.searchUsersAdvanced(username, email, mobileNumber, country, firstName, lastName, applicationIds, authorityIds, pageable);
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

                // ✅ Update new fields
                existing.setUsername(dto.getUsername());

                existing.setEmail(dto.getEmail());
                existing.setMobileNumber(dto.getMobileNumber());

                if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                    existing.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
                }

                // Activated / Activation Key / Auth Status
                existing.setActivated(dto.getActivated());
                existing.setActivationKey(dto.getActivationKey());
                existing.setAuthStatus(dto.getAuthStatus());

                // Profile info
                existing.setCountry(dto.getCountry());
                existing.setFirstName(dto.getFirstName());
                existing.setLastName(dto.getLastName());
                existing.setLangKey(dto.getLangKey());

                // Reset key (if provided)
                existing.setResetKey(dto.getResetKey());

                // Status (enum mapping)
                if (dto.getStatus() != null) {
                    existing.setStatus(UserCredential.Status.valueOf(dto.getStatus().toUpperCase()));
                }

                // Applications
                if (dto.getApplicationIds() != null && !dto.getApplicationIds().isEmpty()) {
                    Set<Application> applications = new HashSet<>(applicationRepository.findAllById(dto.getApplicationIds()));
                    if (applications.isEmpty()) {
                        throw new IllegalArgumentException("Invalid Application IDs");
                    }
                    existing.setApplications(applications);
                }

                // Authorities
                if (dto.getAuthorities() != null) {
                    Set<Authority> authorities = dto.getAuthorities().stream()
                            .map(authId -> authorityRepository.findById(authId)
                                    .orElseThrow(() -> new NoSuchElementException("Authority not found with id " + authId)))
                            .collect(Collectors.toSet());
                    existing.setAuthorities(authorities);
                }

                // Update last modified timestamp
                existing.setLastModifyDate(LocalDateTime.now());

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

        @Override
        public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
            Map<String, Object> response = new HashMap<>();
            try {
                log.info("[UserServiceImpl] Attempting to change password for username: {}", request.getUserName());

                // ✅ Step 1: Validate input
                if (request.getUserName() == null || request.getOldPassword() == null || request.getNewPassword() == null) {
                    log.warn("[UserServiceImpl] Missing required fields");
                    response.put("message", "All fields are required");
                    return ResponseEntity.badRequest().body(response);
                }

                // ✅ Step 2: Find user by username
                UserCredential user = repository.findByUsername(request.getUserName()).orElse(null);
                if (user == null) {
                    log.warn("[UserServiceImpl] User not found for username: {}", request.getUserName());
                    response.put("message", "User not found with given username");
                    return ResponseEntity.status(404).body(response);
                }

                // ✅ Step 3: Verify old password
                if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
                    log.warn("[UserServiceImpl] Old password is incorrect for username: {}", request.getUserName());
                    response.put("message", "Old password is incorrect");
                    return ResponseEntity.badRequest().body(response);
                }

                // ✅ Step 4: Update to new password
                String hashedPassword = passwordEncoder.encode(request.getNewPassword());
                user.setPasswordHash(hashedPassword);
                user.setLastModifyDate(LocalDateTime.now());
                repository.save(user);

                log.info("[UserServiceImpl] Password changed successfully for username: {}", request.getUserName());
                response.put("message", "Password changed successfully!");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                log.error("[UserServiceImpl] Error changing password: {}", e.getMessage(), e);
                response.put("message", "Internal server error");
                return ResponseEntity.internalServerError().body(response);
            }
        }
    }
