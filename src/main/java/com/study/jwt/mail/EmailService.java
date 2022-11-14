package com.study.jwt.mail;

import com.study.jwt.redis.domain.UserEmail;

public interface EmailService {

    void  sendEmail(EmailMessage emailMessage);

    // Email 인증 확인
    boolean checkCertifyEmail(UserEmail userEmail);
}