package com.identity.service;

import com.identity.entity.UserCredential;
import com.identity.reository.UserRepository;
import com.identity.serviceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;


    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiresAt;

        OtpData(String otp, LocalDateTime expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }
    }

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
          String otp = "123456"; // 6 digits
//        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // 6 digits

        otpStore.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(10))); // valid 10 min
        return otp;
    }

    public String verifyOtp(String userName, String otp) {
        log.info("[verifyOtp] Verifying OTP for userName: {}", userName);

        if (userName == null || otp == null || userName.isEmpty() || otp.isEmpty()) {
            log.warn("[verifyOtp] Username and OTP are required");
            return "Username and OTP are required";
        }

        try {
            // ✅ Step 1: Find user by username
            Optional<UserCredential> userOpt = userRepository.findByUsername(userName);
            if (userOpt.isEmpty()) {
                log.warn("[verifyOtp] User not found for username: {}", userName);
                return "User not found";
            }

            UserCredential user = userOpt.get();

            // ✅ Step 2: Ensure user has a valid email
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                log.warn("[verifyOtp] No email found for username: {}", userName);
                return "No email associated with this user";
            }

            String email = user.getEmail();

            // ✅ Step 3: Check OTP data
            OtpData data = otpStore.get(email);
            if (data == null) {
                log.warn("[verifyOtp] OTP not found for {}", email);
                return "OTP not found or expired";
            }

            // ✅ Step 4: Check expiration
            if (LocalDateTime.now().isAfter(data.expiresAt)) {
                otpStore.remove(email);
                log.warn("[verifyOtp] OTP expired for {}", email);
                return "OTP expired";
            }

            // ✅ Step 5: Validate OTP
            if (!data.otp.equals(otp)) {
                log.warn("[verifyOtp] Invalid OTP for {}", email);
                return "Invalid OTP";
            }

            // ✅ Step 6: Success — remove OTP after verification
            otpStore.remove(email);
            log.info("[verifyOtp] OTP verified successfully for {}", email);
            return "OTP verified successfully";

        } catch (Exception e) {
            log.error("[verifyOtp] Error verifying OTP: {}", e.getMessage());
            return "Internal server error";
        }
    }


}
