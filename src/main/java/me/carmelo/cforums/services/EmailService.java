package me.carmelo.cforums.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import me.carmelo.cforums.models.user.entity.User;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerificationEmail(String username, String email, String token) throws MessagingException {
        String subject = "Verify your email";

        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token + "&email=" + email;

        Context context = new Context();
        context.setVariable("user", username);
        context.setVariable("verificationUrl", verificationUrl);

        String html = templateEngine.process("verification-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }
}
