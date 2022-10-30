package com.study.jwt.service;

import com.study.jwt.redis.domain.UserSMS;

import java.util.List;

public interface SMSService {
    // SMS 인증 문자 전송 및 redis 저장
    void sendCertifySMS(String from_phone, String certify_code);

    // SMS 인증 문자 확인
    boolean checkCertifySMS(String phone, String certify_code);

    // userSMS 테이블 전체 가져오기
    List<UserSMS> findAllUserSMS();
}
