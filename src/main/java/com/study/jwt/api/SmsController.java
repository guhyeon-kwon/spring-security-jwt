package com.study.jwt.api;

import com.google.inject.internal.Errors;
import com.study.jwt.redis.domain.UserSMS;
import com.study.jwt.service.SMSService;
import com.study.jwt.utils.ReturnObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.study.jwt.utils.CommonUtil.getRandomNumber;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SMSService service;

    @GetMapping("/list")
    public ResponseEntity<ReturnObject> getAllUserSMS(){
        ReturnObject object = ReturnObject.builder()
                .msg("ok")
                .data(service.findAllUserSMS()).build();
        return ResponseEntity.ok().body(object);
    }

    @PostMapping("/certify-regis")
    public ResponseEntity<ReturnObject> certifyUser(@RequestBody UserSMS userSMS, Errors errors){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/sms/certify-regis").toUriString());
        String code = getRandomNumber(4);
        service.sendCertifySMS(userSMS.getPhone(), code);
        userSMS.setCode(code);
        ReturnObject object = ReturnObject.builder()
                .msg("ok")
                .data(userSMS).build();
        return ResponseEntity.ok().body(object);
    }
}
