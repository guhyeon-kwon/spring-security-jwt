package com.study.jwt.service;

import com.study.jwt.redis.domain.UserSMS;
import com.study.jwt.redis.repo.UserSMSRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SMSServiceImpl implements SMSService {

    private final UserSMSRepo userSMSRepo;

    String api_key = "NCSCNGQWTYMTSURP";
    String api_secret = "CIMMFR6MNPYFWE5HQSF4EQZTOYDFL3B2";
    String to_phone = "01057444274";

    @Override
    public void sendCertifySMS(String from_phone, String certify_code) {
        UserSMS userSMS = UserSMS.builder().phone(from_phone).code(certify_code).purpose("regis").build();

        String message = String.format("[Spring] 인증번호 %s 를 입력하세요.", certify_code);

        try {
            sendSMS(from_phone, message);
        } catch (CoolsmsException e) {
            log.error(e.getMessage());
            log.error(String.valueOf(e.getCode()));
        }

        try {
            userSMSRepo.save(userSMS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean checkCertifySMS(String phone, String certify_code, String purpose) {
        Optional<UserSMS> user = userSMSRepo.findByPhoneAndPurpose(phone, purpose);
        if (user.isEmpty()) {
            return false;
        }
        UserSMS userSMS = user.get();
        if (!userSMS.getCode().equals(certify_code)) {
            return false;
        }
        return true;
    }

    @Override
    public List<UserSMS> findAllUserSMS() {
        return (List<UserSMS>) userSMSRepo.findAll();
    }



    // 문자 전송
    private JSONObject sendSMS(String from_phone, String message) throws CoolsmsException {
        Message coolsms = new Message(api_key, api_secret);

        // send
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", to_phone); // 수신번호
        params.put("from", from_phone); // 발신번호
        params.put("type", "SMS"); // Message type ( SMS, LMS, MMS, ATA )
        params.put("text", message); // 문자내용
        params.put("mode", "test");

//        JSONObject obj = (JSONObject) coolsms.send(params);

        return new JSONObject();
    }
}
