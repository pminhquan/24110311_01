package vn.iotstar.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailUtil_24110311 {

    // Gmail SMTP Configuration
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    /**
     * Retrieves the sender email from System properties or Environment variables.
     */
    public static String getSenderEmail() {
        String email = System.getProperty("mail.from");
        if (email == null || email.trim().isEmpty()) {
            email = System.getenv("MAIL_FROM");
        }
        return email != null ? email.trim() : "";
    }

    /**
     * Retrieves and sanitizes the 16-character App Password (removes any spaces).
     */
    public static String getAppPassword() {
        String password = System.getProperty("mail.password");
        if (password == null || password.trim().isEmpty()) {
            password = System.getenv("MAIL_PASSWORD");
        }
        return password != null ? password.trim().replaceAll("\\s+", "") : "";
    }

    public static boolean sendOtp(String toEmail, String otp) {
        String fromEmail = getSenderEmail();
        String appPassword = getAppPassword();

        if (fromEmail.isEmpty() || fromEmail.contains("your-email") 
                || appPassword.isEmpty() || appPassword.contains("your-app-password")) {
            System.err.println(">>> [EMAIL CONFIG ERROR] Sender email or App Password not configured.");
            System.err.println(">>> [EMAIL HINT] Please set mail.from and mail.password via System properties (-Dmail.from=... -Dmail.password=...) or environment variables (MAIL_FROM, MAIL_PASSWORD).");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã xác thực OTP đăng ký tài khoản");
            message.setText("Xin chào,\n\nMã OTP xác thực đăng ký tài khoản của bạn là: " + otp
                    + "\n\nMã OTP có hiệu lực trong phiên đăng ký hiện tại.\n\nTrân trọng!");

            Transport.send(message);
            System.out.println(">>> [EMAIL SUCCESS] OTP successfully sent to: " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.err.println(">>> [EMAIL ERROR] Failed to send email to " + toEmail + ": " + e.getMessage());
            if (e.getMessage() != null && (e.getMessage().contains("535") || e.getMessage().contains("Username and Password not accepted"))) {
                System.err.println(">>> [EMAIL HINT] Gmail rejected credentials (535-5.7.8). Ensure 2-Step Verification is ON and you are using a 16-character Google App Password (not your regular Gmail password).");
            }
            return false;
        }
    }
}
