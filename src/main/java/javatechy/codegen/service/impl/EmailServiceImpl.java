package javatechy.codegen.service.impl;

import jakarta.mail.internet.MimeMessage;
import javatechy.codegen.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Password Reset Request");


            String htmlContent = "<html>"
                    + "<body>"
                    + "<p>Hello,</p>"
                    + "<p>You have requested to reset your password. Click the link below to reset it:</p>"
                    + "<p><a href=\"" + resetLink + "\">Reset Password</a></p>"
                    + "<p>This link will expire in 24 hours.</p>"
                    + "<p>If you didn't request this change, please ignore this email.</p>"
                    + "<p>Best regards,<br>Your App Team</p>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}

