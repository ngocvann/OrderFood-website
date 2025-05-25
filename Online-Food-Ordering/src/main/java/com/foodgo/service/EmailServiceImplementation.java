package com.foodgo.service;

import com.foodgo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Value("${frontend.verification.url}")
    private String frontendBaseUrl;

    @Override
    public void sendVerificationEmail(User user, String token) {
        String subject = "Xác thực tài khoản FoodGO!";
        String confirmationUrl = frontendBaseUrl + "/account/verify?token=" + token;
        String body = "Chào " + user.getFullName() + ",\n\n" +
                "Vui lòng nhấp vào liên kết bên dưới để xác thực tài khoản:\n" + confirmationUrl +
                "\n\nLiên kết này sẽ hết hạn sau 10 phút.\n\nFoodGO! Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
