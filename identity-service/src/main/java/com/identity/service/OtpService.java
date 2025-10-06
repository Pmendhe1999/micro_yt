package com.identity.service;

import com.identity.serviceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


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
        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // 6 digits
        otpStore.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(10))); // valid 10 min
        return otp;
    }

    // ✅ Verify OTP (called by controller)
    public String verifyOtp(String email, String userName, String otp) {
        log.info("[verifyOtp] Verifying OTP for email: {}, userName: {}", email, userName);

        if (email == null || otp == null || email.isEmpty() || otp.isEmpty()) {
            log.warn("[verifyOtp] Email and OTP are required");
            return "Email and OTP are required";
        }

        try {
            // ✅ Step 1: Check if user exists
            String query = "SELECT id, email FROM users WHERE email = ? AND username = ? LIMIT 1";
            Map<String, Object> user = jdbcTemplate.queryForMap(query, email, userName);

            if (user == null || user.isEmpty()) {
                log.warn("[verifyOtp] User not found for email {}", email);
                return "User not found";
            }

            // ✅ Step 2: Check if OTP exists and is valid
            OtpData data = otpStore.get(email);
            if (data == null) {
                log.warn("[verifyOtp] No OTP found for {}", email);
                return "OTP not found or expired";
            }

            if (LocalDateTime.now().isAfter(data.expiresAt)) {
                otpStore.remove(email);
                log.warn("[verifyOtp] OTP expired for {}", email);
                return "OTP expired";
            }

            if (!data.otp.equals(otp)) {
                log.warn("[verifyOtp] Invalid OTP for {}", email);
                return "Invalid OTP";
            }

            // ✅ Step 3: OTP verified successfully
            otpStore.remove(email);
            log.info("[verifyOtp] OTP verified successfully for {}", email);
            return "OTP verified successfully";

        } catch (Exception e) {
            log.error("[verifyOtp] Error verifying OTP: {}", e.getMessage());
            return "Internal server error";
        }
    }




}
