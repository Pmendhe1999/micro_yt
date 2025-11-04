package com.identity.serviceImpl;

import com.identity.dto.ChangePasswordRequest;
import com.identity.dto.UserRegisterDto;
import com.identity.entity.*;
import com.identity.reository.*;
import com.identity.service.JwtService;
import com.identity.service.OtpService;
import com.identity.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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
        private OtpService otpService;

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

        @Autowired
        private AuthTypeMasterRepository authTypeMasterRepository;

        @Autowired
        private EntityManager entityManager;


        // CREATE
        @Override
        public UserCredential saveUser(UserRegisterDto dto) {
            try {
                // 🔹 Generate a random password
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

                // 🔹 Create user entity
                UserCredential credential = new UserCredential();
                credential.setUsername(dto.getUsername());
                credential.setEmail(dto.getEmail());
                credential.setCountry(dto.getCountry());
                credential.setMobileNumber(dto.getMobileNumber());
                credential.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

                credential.setFirstName(dto.getFirstName());
                credential.setLastName(dto.getLastName());

                // ✅ Defaults
                credential.setActivated(true);
                credential.setAuthStatus(true);
                credential.setStatus(UserCredential.Status.ACTIVE);
                credential.setCreatedDate(LocalDateTime.now());
                credential.setLastModifyDate(LocalDateTime.now());
                credential.setResetDate(null);

                // ✅ Map application if provided
                if (dto.getApplicationIds() != null && !dto.getApplicationIds().isEmpty()) {
                    Set<Application> applications = new HashSet<>(applicationRepository.findAllById(dto.getApplicationIds()));
                    if (applications.isEmpty()) {
                        throw new IllegalArgumentException("Invalid Application IDs");
                    }
                    credential.setApplications(applications);
                }

                // ✅ Map authorities if provided
                if (dto.getAuthorities() != null && !dto.getAuthorities().isEmpty()) {
                    Set<Authority> authorities = new HashSet<>(authorityRepository.findAllById(dto.getAuthorities()));
                    credential.setAuthorities(authorities);
                }

                // 🔹 Determine Application and AuthType
                Long appId = (dto.getApplicationIds() != null && !dto.getApplicationIds().isEmpty())
                        ? dto.getApplicationIds().iterator().next()
                        : null;

                Application application = (appId != null)
                        ? applicationRepository.findById(appId).orElse(null)
                        : null;

                AppFunTypesMaster regFunType = appFunTypesMasterRepository.findByNameIgnoreCase("Registration").orElse(null);

                AppFunction appFunction = (regFunType != null && application != null)
                        ? appFunctionRepository.findByAppFunTypesMasterAndApplication(regFunType, application).orElse(null)
                        : null;

                AuthType authType = (appFunction != null)
                        ? authTypeRepository.findByAppFunction(appFunction).orElse(null)
                        : null;

                AuthTypeMaster authTypeMaster = (authType != null)
                        ? authType.getAuthTypeMaster()
                        : authTypeMasterRepository.findByNameIgnoreCase("self-authentication").orElse(null);

                // ✅ Set flags based on AuthTypeMaster
                if (authTypeMaster != null &&
                        authTypeMaster.getName().equalsIgnoreCase("self-authentication")) {
                    credential.setSelfAuthentication(true);
                    credential.setActivationKey(true);
                    credential.setActivated(true);

                } else {
                    credential.setSelfAuthentication(false);

                }

                if (authTypeMaster != null &&
                        authTypeMaster.getName().equalsIgnoreCase("otp-authentication")) {
                    credential.setOtpAuthentication(true);
                    credential.setSelfAuthentication(true);
                    credential.setActivationKey(true);
                    credential.setActivated(true);
                } else {
                    credential.setOtpAuthentication(false);
                    credential.setSelfAuthentication(false);
                }

                if (authTypeMaster != null &&
                        authTypeMaster.getName().equalsIgnoreCase("default password")) {
                    credential.setActivationKey(true);
                }
                if (authTypeMaster != null &&
                        authTypeMaster.getName().equalsIgnoreCase("Admin Authentication")) {
                    credential.setActivationKey(false);
                    credential.setActivated(false);
                }

                // ✅ Save user
                UserCredential saved = repository.save(credential);
                log.info("User '{}' created successfully", saved.getUsername());

                // ✅ Behavior based on AuthTypeMaster name
                String authTypeName = (authTypeMaster != null) ? authTypeMaster.getName().toLowerCase() : "";

                switch (authTypeName) {
                    case "email_notification" -> {
                        String loginUrl = "http://yourdomain.com/login";
                        emailService.sendCredentialsEmail(
                                saved.getEmail(),
                                saved.getUsername(),
                                dto.getPassword(), // send plain password in email
                                loginUrl
                        );
                        log.info("📧 Credentials email sent to '{}'", saved.getEmail());
                    }

                    case "otp-authentication" -> {
                        // 🔹 Generate OTP and send to email
                        String loginUrl = "http://yourdomain.com/login";
                        emailService.sendCredentialsEmail(
                                saved.getEmail(),
                                saved.getUsername(),
                                dto.getPassword(), // send plain password in email
                                loginUrl
                        );
                        log.info("📧 Credentials email sent to '{}'", saved.getEmail());

//                        String otp = otpService.generateOtp(saved.getEmail());
//                        emailService.sendOtpEmail(saved.getEmail(), otp);
//                        log.info("🔐 OTP authentication selected — OTP sent to {}", saved.getEmail());
                    }

                    case "admin authentication" -> {
                        // 🔹 Find SuperAdmin
                        Optional<UserCredential> superAdminOpt = repository.findByAuthorityName("superadmin");

                        if (superAdminOpt.isPresent()) {
                            UserCredential superAdmin = superAdminOpt.get();
                            String loginUrl = "http://yourdomain.com/login";

                            emailService.sendUserCreatedToSuperAdminEmail(
                                    superAdmin.getEmail(),
                                    saved.getUsername(),
                                    dto.getPassword(),
                                    saved.getEmail(),
                                    loginUrl
                            );

                            log.info("📩 Notification email sent to SuperAdmin ({})", superAdmin.getEmail());
                        } else {
                            log.warn("⚠️ No SuperAdmin found — skipping notification email");
                        }
                    }

                    case "default password" -> {
                        // 🔹 Send credentials directly to user
                        String loginUrl = "http://yourdomain.com/login";
                        emailService.sendCredentialsEmail(
                                saved.getEmail(),
                                saved.getUsername(),
                                dto.getPassword(), // send plain password in email
                                loginUrl
                        );
                        log.info("📧 Default Password email sent to '{}'", saved.getEmail());
                    }


                    default -> log.info("AuthTypeMaster is '{}', no special action triggered", authTypeName);
                }



                return saved;

            } catch (Exception e) {
                log.error("❌ Error while creating User: {}", e.getMessage(), e);
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
                Boolean activationKey,
                Boolean activated,
                Pageable pageable) {

            return repository.searchUsersAdvanced(username, email, mobileNumber, country, firstName, lastName, applicationIds, authorityIds,activationKey, activated, pageable);
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

        @Override
        public UserCredential activateUser(Long id, Boolean activated, String token) {
            try {
                UserCredential existing = repository.findByUserId(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));

                String modifiedByUser = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);

                if (activated) {
                    // ✅ Activate user
                    if (Boolean.TRUE.equals(existing.getActivationKey())) {
                        log.info("User id={} already activated.", id);
                        return existing;
                    }

                    String generatedPassword = generateRandomPassword();
                    existing.setPasswordHash(passwordEncoder.encode(generatedPassword));
                    existing.setActivationKey(true);
                    existing.setActivated(true);
                    existing.setAuthStatus(true);
                    existing.setLastModifyDate(LocalDateTime.now());

                    UserCredential saved = repository.save(existing);
                    log.info("✅ User id={} activated by {} (role={})", saved.getUserId(), modifiedByUser, role);

                    // Send activation email
                    String loginUrl = "http://yourdomain.com/login";
                    emailService.sendCredentialsEmail(
                            saved.getEmail(),
                            saved.getUsername(),
                            generatedPassword,
                            loginUrl
                    );

                    log.info("📧 Activation email sent to '{}'", saved.getEmail());
                    return saved;

                } else {
                    // ❌ Deactivate / Cancel user
                    existing.setActivationKey(true);
                    existing.setActivated(false);
                    existing.setAuthStatus(false);
                    existing.setLastModifyDate(LocalDateTime.now());

                    UserCredential saved = repository.save(existing);
                    log.info("❌ User id={} registration cancelled by {} (role={})", saved.getUserId(), modifiedByUser, role);

                    // Send cancellation email
                    emailService.sendCancellationEmail(saved.getEmail(), saved.getUsername());

                    log.info("📧 Cancellation email sent to '{}'", saved.getEmail());
                    return saved;
                }

            } catch (Exception e) {
                log.error("Error processing activation for user id={}: {}", id, e.getMessage(), e);
                throw e;
            }
        }


        @Override
        @Transactional
        public UserCredential patchUser(Long id, String key, Object value, String token) {
            // Build dynamic SQL query
            String sql = "UPDATE users SET " + key + " = :value, last_modify_date = NOW() WHERE user_id = :id";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("value", value);
            query.setParameter("id", id);

            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new NoSuchElementException("User not found with id " + id);
            }

            // Return updated user
            return repository.findByUserId(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found after update"));
        }
    }
