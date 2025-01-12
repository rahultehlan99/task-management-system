package com.example.taskmanagementsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.enabled:false}")
    private boolean mailEnabled;

    @Override
    @Async("mailThreadPoolTaskExecutor")
    public void sendMail(String body, String recipients) {
        if(!mailEnabled) {
            log.info("Mail service not enabled");
            return;
        }
        try {
            log.info("Sending mail via thread : {}", Thread.currentThread().getName());
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("rtehlan2200@gmail.com");
            simpleMailMessage.setTo(recipients);
            simpleMailMessage.setSubject("Task status");
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e){
            log.warn("Exception occurred while sending mail ", e);
        }
    }
}
