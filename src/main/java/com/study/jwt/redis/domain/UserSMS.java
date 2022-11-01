package com.study.jwt.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter @Setter
@RedisHash(timeToLive = 180)
@AllArgsConstructor
@Builder
public class UserSMS {
    @Id
    private String id;

    @Indexed
    private String phone;

    private String code;

    // 용도 ex) 회원가입, 비밀번호 찾기, 아이디 찾기
    @Indexed
    private String purpose;
}
