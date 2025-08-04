package com.example.email_sender.service;

import com.example.email_sender.DTO.EmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${email.from}")
    private String FROM;

    @Async("taskExecutor")
    public void sendRestoreCode(EmailDTO emailDTO){
        try{
            send(emailDTO.getEmail(), "Recovery code", emailDTO.getCode());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void send(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
