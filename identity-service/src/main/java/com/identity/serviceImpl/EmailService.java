package com.identity.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

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
}
