package com.identity.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OtpStore {
    private static final Map<String, OtpData> otpMap = new ConcurrentHashMap<>();

    public static void storeOtp(String email, String otp, long expiryMillis) {
        otpMap.put(email, new OtpData(otp, System.currentTimeMillis() + expiryMillis));
    }

    public static OtpData getOtpData(String email) {
        return otpMap.get(email);
    }

    public static void removeOtp(String email) {
        otpMap.remove(email);
    }

    public static class OtpData {
        private final String otp;
        private final long expiresAt;

        public OtpData(String otp, long expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpiresAt() {
            return expiresAt;
        }
    }
}
