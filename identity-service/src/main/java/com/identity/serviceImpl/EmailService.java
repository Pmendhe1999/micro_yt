package com.identity.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);


    public void sendCredentialsEmail(String toEmail, String username, String password, String loginUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // This line may throw UnsupportedEncodingException, so we wrap in try/catch
            helper.setFrom("itariumtechmail@gmail.com", "Ethosh Company");
            helper.setTo(toEmail);
            helper.setSubject("Your Client Registration Details");

            String content = """
                    <h2>Welcome to Our System!</h2>
                    <p>Your client account has been successfully created. Below are your login details:</p>
                    <ul>
                        <li><b>Username (Email):</b> %s</li>
                        <li><b>Password:</b> %s</li>
                        <li><b>Login URL:</b> <a href="%s">%s</a></li>
                    </ul>
                    <p>Please change your password after logging in for security purposes.</p>
                    <p>Best regards, <br>Aguapro Team</p>
                    """.formatted(username, password, loginUrl, loginUrl);

            helper.setText(content, true); // HTML format

            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("❌ Failed to send email: " + e.getMessage(), e);
        }
    }
    // ✅ New Method for cancellation email
    public void sendCancellationEmail(String toEmail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("itariumtechmail@gmail.com", "Ethosh Company");
            helper.setTo(toEmail);
            helper.setSubject("Registration Cancelled");

            String content = """
                    <h2>Hello %s,</h2>
                    <p>We regret to inform you that your registration request has been <b>cancelled</b>.</p>
                    <p>If you believe this was a mistake, please contact our support team for assistance.</p>
                    <br>
                    <p>Thank you,<br>Aguapro Team</p>
                    """.formatted(username);

            helper.setText(content, true);
            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("❌ Failed to send cancellation email: " + e.getMessage(), e);
        }
    }

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Sender info
            helper.setFrom("itariumtechmail@gmail.com", "Ethosh Company");
            helper.setTo(toEmail);
            helper.setSubject("Your One-Time Password (OTP) for Password Reset");

            // Email body
            String content = """
                    <h2>Password Reset Request</h2>
                    <p>Dear User,</p>
                    <p>We have received a request to reset your password. Please use the following One-Time Password (OTP) to complete the process:</p>
                    <h3 style="color: #2E86C1;">%s</h3>
                    <p>This OTP is valid for <b>10 minutes</b>.</p>
                    <p>If you did not request this, please ignore this email.</p>
                    <br>
                    <p>Best regards,<br><b>Ethosh Security Team</b></p>
                    """.formatted(otp);

            helper.setText(content, true); // enable HTML

            mailSender.send(message);

            log.info("✅ OTP email sent successfully to {}", toEmail);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("❌ Failed to send OTP email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }

    public void sendPasswordResetSuccessEmail(String toEmail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("itariumtechmail@gmail.com", "Ethosh Company");
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Successful");

            String content = """
                <h2>Password Reset Successful</h2>
                <p>Dear %s,</p>
                <p>Your password has been successfully reset.</p>
                <p>If you did not request this change, please contact support immediately.</p>
                <br>
                <p>Best regards,<br><b>Ethosh Security Team</b></p>
                """.formatted(username != null ? username : "User");

            helper.setText(content, true);

            mailSender.send(message);
            log.info("✅ Password reset confirmation email sent to {}", toEmail);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("❌ Failed to send password reset confirmation email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage(), e);
        }
    }

    public void sendUserCreatedToSuperAdminEmail(String toEmail, String newUsername, String password, String newUserEmail, String loginUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("itariumtechmail@gmail.com", "Ethosh Company");
            helper.setTo(toEmail);
            helper.setSubject("New User Created Notification");

            String content = """
                <h2>New User Created</h2>
                <p>A new user has been registered in the system.</p>
                <ul>
                    <li><b>Username:</b> %s</li>
                    <li><b>Email:</b> %s</li>
                    <li><b>Login URL:</b> <a href="%s">%s</a></li>
                </ul>
                <p>Please review and activate if needed.</p>
                <p>Best regards,<br>System Notification Service</p>
                """.formatted(newUsername, newUserEmail, loginUrl, loginUrl);

            helper.setText(content, true);
            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("❌ Failed to send SuperAdmin email: " + e.getMessage(), e);
        }
    }

}
