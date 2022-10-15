package com.study.jwt.service;

import com.study.jwt.domain.EmailMessage;

public interface EmailService {

    void sendEmail(EmailMessage emailMessage);
}