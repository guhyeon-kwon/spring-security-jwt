package com.study.jwt.utils;

import com.study.jwt.domain.EmailMessage;
import com.study.jwt.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

@Profile({"dev", "local"})
@Component
@Slf4j
public class ConsoleMailSender implements EmailService {
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("sent email: {}", emailMessage.getMessage());
    }
}
