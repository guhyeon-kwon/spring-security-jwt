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
        Message coolsms = new Message(api_key, api_secret);

        // send
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", to_phone); // 수신번호
        params.put("from", from_phone); // 발신번호
        params.put("type", "SMS"); // Message type ( SMS, LMS, MMS, ATA )
        params.put("text", String.format("[Spring] 인증번호 %s 를 입력하세요.", certify_code)); // 문자내용
        params.put("mode", "test");

        UserSMS userSMS = UserSMS.builder().phone(from_phone).code(certify_code).build();

        try {
//            JSONObject obj = (JSONObject) coolsms.send(params);
//            System.out.println(obj.toString());
            userSMSRepo.save(userSMS);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public boolean checkCertifySMS(String phone, String certify_code) {
        Optional<UserSMS> user = userSMSRepo.findByPhone(phone);
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
}
